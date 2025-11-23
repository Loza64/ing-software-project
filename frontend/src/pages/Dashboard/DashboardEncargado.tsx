import React, { useState, useEffect, useMemo, FormEvent } from 'react';
import {
    crearUsuario,
    listarUsuarios,
    actualizarUsuario,
    eliminarUsuario
} from '../../services/userService';
import {
    crearMateria,
    listarMaterias,
    actualizarMateria,
    eliminarMateria
} from '../../services/materiaService';
import {
    asociarUsuarioConMateria,
    eliminarAsociacion,
    listarMateriasPorUsuario,
    listarUsuariosPorMateria
} from '../../services/usuarioMateriaService';
import type {
    UsuarioConMaterias,
    UsuarioDTO,
    Materia,
} from '../../types';
import { Plus, Trash2, Edit2, X } from 'lucide-react';

// ❌ Eliminamos react-toastify
// import { toast } from 'react-toastify';
// import 'react-toastify/dist/ReactToastify.css';

// ✔ Usamos tu sistema de notificaciones
import { useNotification } from "../../components/notifications/NotificationContext";

import useAuth from '../../hooks/useAuth';

const ITEMS_PER_PAGE = 10;

const DashboardEncargado: React.FC = () => {

    const { notifySuccess, notifyError } = useNotification();
    const { user: currentUser, updateUser } = useAuth();

    const [searchUsuario, setSearchUsuario] = useState('');
    const [userPage, setUserPage] = useState(1);
    const [usuarios, setUsuarios] = useState<UsuarioConMaterias[]>([]);

    const [modalUserOpen, setModalUserOpen] = useState(false);
    const [editingUser, setEditingUser] = useState<UsuarioConMaterias | null>(null);
    const [userForm, setUserForm] = useState<UsuarioDTO>({
        nombre: '',
        apellido: '',
        correo: '',
        contrasena: '',
        rol: 'ENCARGADO',
        codigoUsuario: ''
    });
    const [userError, setUserError] = useState<string | null>(null);

    const [materiaSearch, setMateriaSearch] = useState('');
    const [selectedMaterias, setSelectedMaterias] = useState<string[]>([]);

    const [searchMateria, setSearchMateria] = useState('');
    const [matPage, setMatPage] = useState(1);
    const [materias, setMaterias] = useState<Materia[]>([]);

    const [modalMatOpen, setModalMatOpen] = useState(false);
    const [editingMat, setEditingMat] = useState<Materia | null>(null);
    const [matForm, setMatForm] = useState<{ nombreMateria: string }>({ nombreMateria: '' });
    const [matError, setMatError] = useState<string | null>(null);

    // ============================
    //  LISTADO DE USUARIOS
    // ============================
    const fetchUsuarios = async () => {
        try {
            const res = await listarUsuarios();
            const usuariosData = res.data;

            const usuariosConMaterias = await Promise.all(
                usuariosData.map(async (u) => {
                    try {
                        const materiasRes = await listarMateriasPorUsuario(String(u.idUsuario));

                        const materiaNombres = materiasRes.data.map((m: any) => m.nombreMateria);

                        return {
                            ...u,
                            materiaIds: materiaNombres,
                        } as UsuarioConMaterias;

                    } catch (error) {
                        console.error(`Error obteniendo materias para usuario ${u.idUsuario}:`, error);
                        notifyError("Error obteniendo materias del usuario ❌");

                        return {
                            ...u,
                            materiaIds: []
                        };
                    }
                })
            );

            setUsuarios(usuariosConMaterias);

        } catch (error) {
            console.error('Error al cargar usuarios:', error);
            notifyError('Error al cargar usuarios ❌');
        }
    };

    // ============================
    //  LISTADO DE MATERIAS
    // ============================
    const fetchMaterias = () => {
        listarMaterias()
            .then(res => setMaterias(res.data))
            .catch(() => notifyError('Error al cargar materias ❌'));
    };

    useEffect(() => {
        fetchUsuarios();
        fetchMaterias();
    }, []);

    // ============================
    //  CHECKBOX DE MATERIAS
    // ============================
    const handleMateriaCheckbox = (id: string) => {
        setSelectedMaterias(prev =>
            prev.includes(id) ? prev.filter(m => m !== id) : [...prev, id]
        );
    };

    // ============================
    //  CREAR / EDITAR USUARIO
    // ============================
    const handleSubmitUser = async (e: FormEvent) => {
        e.preventDefault();
        setUserError(null);

        const isDuplicate = usuarios.some(
            u => u.codigoUsuario === userForm.codigoUsuario &&
                 (!editingUser || u.idUsuario !== editingUser.idUsuario)
        );

        if (isDuplicate) {
            setUserError("Ya existe un usuario con ese código.");
            notifyError("Ya existe un usuario con ese código ❌");
            return;
        }

        const dto: UsuarioDTO = { ...userForm };

        try {
            const res = editingUser
                ? await actualizarUsuario(editingUser.idUsuario, dto)
                : await crearUsuario(dto);

            const usuarioId = res.data.idUsuario;

            // ===============================
            //  ASOCIAR MATERIAS
            // ===============================
            if (editingUser) {
                const prev = await listarMateriasPorUsuario(String(editingUser.idUsuario));
                const prevNames = prev.data.map((m: any) => m.nombreMateria);

                await Promise.all(
                    prevNames
                        .filter(name => !selectedMaterias.includes(name))
                        .map(name => {
                            const mToDelete = prev.data.find((m: any) => m.nombreMateria === name);
                            return mToDelete
                                ? eliminarAsociacion(String(mToDelete.idUsuarioXMateria))
                                : Promise.resolve();
                        })
                );

                // nuevas
                const nuevas = selectedMaterias.filter(n => !prevNames.includes(n));
                await Promise.all(
                    nuevas.map(n => asociarUsuarioConMateria(dto.codigoUsuario, n))
                );

            } else {
                await Promise.all(
                    selectedMaterias.map((n) => asociarUsuarioConMateria(dto.codigoUsuario, n))
                );
            }

            notifySuccess(
                editingUser
                    ? "Usuario actualizado con éxito 🎉"
                    : "Usuario creado exitosamente 🎉"
            );

            // actualizar usuario en sesión si corresponde
            if (editingUser && currentUser && editingUser.idUsuario === currentUser.idUsuario) {
                updateUser({
                    ...currentUser,
                    nombre: dto.nombre,
                    apellido: dto.apellido,
                    correo: dto.correo,
                    rol: dto.rol,
                    codigoUsuario: dto.codigoUsuario
                });
            }

            fetchUsuarios();
            setModalUserOpen(false);

        } catch (err: any) {
            console.error("Error al guardar usuario:", err);
            notifyError("Error al guardar usuario ❌");
            setUserError(err.response?.data?.message || err.message);
        }
    };

    // ============================
    //  NUEVO USUARIO
    // ============================
    const openNewUser = () => {
        setEditingUser(null);
        setUserForm({
            nombre: '',
            apellido: '',
            correo: '',
            contrasena: '',
            rol: 'ENCARGADO',
            codigoUsuario: ''
        });
        setSelectedMaterias([]);
        setUserError(null);
        setModalUserOpen(true);
    };

    // ============================
    //  EDITAR
    // ============================
    const openEditUser = async (u: UsuarioConMaterias) => {
        setEditingUser(u);
        setUserForm({
            nombre: u.nombre,
            apellido: u.apellido,
            correo: u.correo,
            contrasena: '',
            rol: u.rol,
            codigoUsuario: u.codigoUsuario
        });

        try {
            const res = await listarMateriasPorUsuario(String(u.idUsuario));
            const materiaNombres = res.data.map((m: any) => m.nombreMateria);
            setSelectedMaterias(materiaNombres);

        } catch (error) {
            console.error("Error obteniendo materias:", error);
            notifyError("Error cargando materias del usuario ❌");
        }

        setUserError(null);
        setModalUserOpen(true);
    };

    // ============================
    //   ELIMINAR USUARIO
    // ============================
    const handleDeleteUser = async (idUsuario: string) => {
        if (!confirm("¿Estás seguro de eliminar este usuario?")) return;

        try {
            await eliminarUsuario(idUsuario);
            notifySuccess("Usuario eliminado correctamente 🗑️");
            fetchUsuarios();

        } catch (error: any) {
            console.error("Error al eliminar usuario:", error);

            if (error.response?.status === 409) {
                const confirmDes = confirm(
                    "Este usuario tiene materias asignadas. ¿Desasignarlas y luego eliminar?"
                );

                if (confirmDes) {
                    try {
                        const materiasRes = await listarMateriasPorUsuario(idUsuario);
                        const asignadas = materiasRes.data;

                        await Promise.all(
                            asignadas.map((m: any) =>
                                eliminarAsociacion(String(m.idUsuarioXMateria))
                            )
                        );

                        await eliminarUsuario(idUsuario);
                        notifySuccess("Usuario eliminado después de desasignar materias 🗑️");

                        fetchUsuarios();

                    } catch (err2: any) {
                        console.error("Error desasignando:", err2);
                        notifyError("Error al desasignar materias ❌");
                    }
                }

            } else if (error.response?.status === 401 || error.response?.status === 403) {
                notifyError("Sesión expirada ❌");

            } else {
                notifyError("Error al eliminar usuario ❌");
            }
        }
    };

    // ============================
    //  CREAR / EDITAR MATERIA
    // ============================
    const handleSubmitMat = (e: FormEvent) => {
        e.preventDefault();
        setMatError(null);

        const nombre = matForm.nombreMateria.trim();
        const exists = materias.some(
            m => m.nombreMateria.toLowerCase() === nombre.toLowerCase() &&
            (!editingMat || m.idMateria !== editingMat.idMateria)
        );

        if (exists) {
            setMatError("Ya existe una materia con ese nombre.");
            notifyError("Ya existe una materia con ese nombre ❌");
            return;
        }

        const action = editingMat
            ? actualizarMateria(editingMat.idMateria, nombre)
            : crearMateria(nombre);

        action
            .then(() => {
                notifySuccess(
                    editingMat
                        ? "Materia actualizada con éxito 🎉"
                        : "Materia creada exitosamente 🎉"
                );
                fetchMaterias();
                setModalMatOpen(false);
            })
            .catch(err => {
                notifyError("Error al guardar materia ❌");
                setMatError(err.response?.data?.message || err.message);
            });
    };

    const openNewMat = () => {
        setEditingMat(null);
        setMatForm({ nombreMateria: '' });
        setMatError(null);
        setModalMatOpen(true);
    };

    const openEditMat = (m: Materia) => {
        setEditingMat(m);
        setMatForm({ nombreMateria: m.nombreMateria });
        setMatError(null);
        setModalMatOpen(true);
    };

    // ============================
    //  ELIMINAR MATERIA
    // ============================
    const handleDeleteMat = async (id: string) => {
        if (!confirm("¿Estás seguro de eliminar esta materia?")) return;

        try {
            await eliminarMateria(id);
            notifySuccess("Materia eliminada correctamente 🗑️");
            fetchMaterias();

        } catch (error: any) {
            console.error("Error al eliminar materia:", error);

            if (error.response?.status === 409) {
                const confirmDes = confirm(
                    "La materia tiene usuarios asignados. ¿Desasignarlos y luego eliminar?"
                );

                if (confirmDes) {
                    try {
                        const usuariosRes = await listarUsuariosPorMateria(id);
                        const asignados = usuariosRes.data;

                        await Promise.all(
                            asignados.map(async (u: any) => {
                                const matsRes = await listarMateriasPorUsuario(String(u.idUsuario));
                                const matToDelete = matsRes.data.find(
                                    (m: any) =>
                                        m.nombreMateria ===
                                        materias.find(mt => String(mt.idMateria) === id)?.nombreMateria
                                );

                                return matToDelete
                                    ? eliminarAsociacion(String(matToDelete.idUsuarioXMateria))
                                    : Promise.resolve();
                            })
                        );

                        await eliminarMateria(id);
                        notifySuccess("Materia eliminada tras desasignar usuarios 🗑️");

                        fetchMaterias();
                        fetchUsuarios();

                    } catch (err2: any) {
                        notifyError("Error al desasignar usuarios ❌");
                    }
                }

            } else if (error.response?.status === 401 || error.response?.status === 403) {
                notifyError("Sesión expirada ❌");

            } else {
                notifyError("Error al eliminar materia ❌");
            }
        }
    };

    const getMateriasNombres = (usuario: UsuarioConMaterias): string => {
        if (!usuario.materiaIds || usuario.materiaIds.length === 0) {
            return 'Sin materias asignadas';
        }
        return usuario.materiaIds.join(', ');
    };

    // =======================================================
    //                  RENDER DEL DASHBOARD
    // =======================================================
    return (
        <div className="space-y-8 p-4">
            {/* ======== USUARIOS ======== */}
            <div className="bg-white rounded-lg shadow p-6 space-y-4">
                <div className="flex justify-between items-center">
                    <h2 className="text-xl font-semibold text-[#003c71]">Gestión de Usuarios</h2>
                    <button
                        onClick={openNewUser}
                        className="flex items-center gap-2 text-white bg-[#003c71] px-4 py-2 rounded hover:bg-[#002f59]"
                    >
                        <Plus size={16} /> Nuevo usuario
                    </button>
                </div>

                <input
                    type="text"
                    placeholder="Buscar usuario..."
                    className="border rounded px-3 py-2 w-full"
                    value={searchUsuario}
                    onChange={e => {
                        setSearchUsuario(e.target.value);
                        setUserPage(1);
                    }}
                />

                <div className="overflow-x-auto">
                    <table className="w-full text-left text-sm">
                        <thead>
                            <tr className="bg-gray-100 text-gray-700">
                                <th className="px-3 py-2">Nombre</th>
                                <th className="px-3 py-2">Email</th>
                                <th className="px-3 py-2">Rol</th>
                                <th className="px-3 py-2">Código</th>
                                <th className="px-3 py-2">Materias</th>
                                <th className="px-3 py-2">Acciones</th>
                            </tr>
                        </thead>

                        <tbody>
                            {paginatedUsuarios.map(u => (
                                <tr key={u.idUsuario} className="border-b hover:bg-gray-50">
                                    <td className="px-3 py-2">{u.nombre} {u.apellido}</td>
                                    <td className="px-3 py-2">{u.correo}</td>
                                    <td className="px-3 py-2">{u.rol}</td>
                                    <td className="px-3 py-2">{u.codigoUsuario}</td>
                                    <td className="px-3 py-2">{getMateriasNombres(u)}</td>

                                    <td className="px-3 py-2 flex gap-2">
                                        <button
                                            onClick={() => openEditUser(u)}
                                            className="text-blue-600"
                                        >
                                            <Edit2 size={16} />
                                        </button>

                                        <button
                                            onClick={() => handleDeleteUser(String(u.idUsuario))}
                                            className="text-red-600"
                                        >
                                            <Trash2 size={16} />
                                        </button>
                                    </td>
                                </tr>
                            ))}

                            {paginatedUsuarios.length === 0 && (
                                <tr>
                                    <td colSpan={6} className="py-4 text-center text-gray-500">
                                        No hay usuarios.
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>

                <div className="flex justify-between items-center mt-4">
                    <span>Página {userPage} de {userPageCount}</span>

                    <div className="flex gap-2">
                        <button
                            onClick={() => setUserPage(p => Math.max(1, p - 1))}
                            disabled={userPage === 1}
                        >
                            Anterior
                        </button>

                        <button
                            onClick={() => setUserPage(p => Math.min(userPageCount, p + 1))}
                            disabled={userPage === userPageCount}
                        >
                            Siguiente
                        </button>
                    </div>
                </div>
            </div>

            {/* ======== MATERIAS ======== */}
            <div className="bg-white rounded-lg shadow p-6 space-y-4">
                <div className="flex justify-between items-center">
                    <h2 className="text-xl font-semibold text-[#003c71]">Gestión de Materias</h2>

                    <button
                        onClick={openNewMat}
                        className="flex items-center gap-2 text-white bg-[#003c71] px-4 py-2 rounded hover:bg-[#002f59]"
                    >
                        <Plus size={16} /> Nueva materia
                    </button>
                </div>

                <input
                    type="text"
                    placeholder="Buscar materia..."
                    className="border rounded px-3 py-2 w-full"
                    value={searchMateria}
                    onChange={e => {
                        setSearchMateria(e.target.value);
                        setMatPage(1);
                    }}
                />

                <div className="overflow-x-auto">
                    <table className="w-full text-left text-sm">
                        <thead>
                            <tr className="bg-gray-100 text-gray-700">
                                <th className="px-3 py-2">Nombre</th>
                                <th className="px-3 py-2">Acciones</th>
                            </tr>
                        </thead>

                        <tbody>
                            {paginatedMaterias.map(m => (
                                <tr key={m.idMateria} className="border-b hover:bg-gray-50">
                                    <td className="px-3 py-2">{m.nombreMateria}</td>

                                    <td className="px-3 py-2 flex gap-2">
                                        <button
                                            onClick={() => openEditMat(m)}
                                            className="text-blue-600"
                                        >
                                            <Edit2 size={16} />
                                        </button>

                                        <button
                                            onClick={() => handleDeleteMat(String(m.idMateria))}
                                            className="text-red-600"
                                        >
                                            <Trash2 size={16} />
                                        </button>
                                    </td>
                                </tr>
                            ))}

                            {paginatedMaterias.length === 0 && (
                                <tr>
                                    <td colSpan={2} className="py-4 text-center text-gray-500">
                                        No hay materias.
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>

                <div className="flex justify-between items-center mt-4">
                    <span>Página {matPage} de {matPageCount}</span>

                    <div className="flex gap-2">
                        <button
                            onClick={() => setMatPage(p => Math.max(1, p - 1))}
                            disabled={matPage === 1}
                        >
                            Anterior
                        </button>

                        <button
                            onClick={() => setMatPage(p => Math.min(matPageCount, p + 1))}
                            disabled={matPage === matPageCount}
                        >
                            Siguiente
                        </button>
                    </div>
                </div>
            </div>

            {/* ======= MODAL USUARIO ======= */}
            {modalUserOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md relative">
                        <button
                            className="absolute top-3 right-3"
                            onClick={() => setModalUserOpen(false)}
                        >
                            <X />
                        </button>

                        <h3 className="text-xl font-semibold mb-4">
                            {editingUser ? "Editar usuario" : "Nuevo usuario"}
                        </h3>

                        <form onSubmit={handleSubmitUser} className="space-y-3">

                            <input
                                type="text"
                                placeholder="Código de usuario"
                                value={userForm.codigoUsuario}
                                onChange={e => setUserForm(f => ({ ...f, codigoUsuario: e.target.value }))}
                                required
                                className="w-full border rounded px-3 py-2"
                            />

                            <input
                                type="text"
                                placeholder="Nombre"
                                value={userForm.nombre}
                                onChange={e => setUserForm(f => ({ ...f, nombre: e.target.value }))}
                                required
                                className="w-full border rounded px-3 py-2"
                            />

                            <input
                                type="text"
                                placeholder="Apellido"
                                value={userForm.apellido}
                                onChange={e => setUserForm(f => ({ ...f, apellido: e.target.value }))}
                                required
                                className="w-full border rounded px-3 py-2"
                            />

                            <input
                                type="email"
                                placeholder="Email"
                                value={userForm.correo}
                                onChange={e => setUserForm(f => ({ ...f, correo: e.target.value }))}
                                required
                                className="w-full border rounded px-3 py-2"
                            />

                            <select
                                value={userForm.rol}
                                onChange={e => setUserForm(f => ({ ...f, rol: e.target.value as any }))}
                                className="w-full border rounded px-3 py-2"
                            >
                                <option value="ENCARGADO">Encargado</option>
                                <option value="INSTRUCTOR_NORMAL">Instructor Social</option>
                                <option value="INSTRUCTOR_REMUNERADO">Instructor Remunerado</option>
                            </select>

                            {!editingUser && (
                                <input
                                    type="password"
                                    placeholder="Contraseña"
                                    value={userForm.contrasena}
                                    onChange={e => setUserForm(f => ({ ...f, contrasena: e.target.value }))}
                                    required
                                    className="w-full border rounded px-3 py-2"
                                />
                            )}

                            <input
                                type="text"
                                placeholder="Buscar materia..."
                                value={materiaSearch}
                                onChange={(e) => setMateriaSearch(e.target.value)}
                                className="w-full border rounded px-3 py-2 mt-4"
                            />

                            <div className="max-h-40 overflow-y-auto border rounded p-2">
                                {materias
                                    .filter(m =>
                                        m.nombreMateria.toLowerCase().includes(materiaSearch.toLowerCase())
                                    )
                                    .map(m => (
                                        <label key={m.idMateria} className="flex items-center space-x-2">
                                            <input
                                                type="checkbox"
                                                checked={selectedMaterias.includes(m.nombreMateria)}
                                                value={m.nombreMateria}
                                                onChange={e => handleMateriaCheckbox(e.currentTarget.value)}
                                            />
                                            <span>{m.nombreMateria}</span>
                                        </label>
                                    ))
                                }
                            </div>

                            {userError && (
                                <p className="text-red-600 text-sm">{userError}</p>
                            )}

                            <button
                                type="submit"
                                className="w-full bg-blue-600 text-white py-2 rounded"
                            >
                                Guardar
                            </button>
                        </form>
                    </div>
                </div>
            )}

            {/* ======= MODAL MATERIA ======= */}
            {modalMatOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg p-6 w-full max-w-sm relative">
                        <button
                            className="absolute top-3 right-3"
                            onClick={() => setModalMatOpen(false)}
                        >
                            <X />
                        </button>

                        <h3 className="text-xl font-semibold mb-4">
                            {editingMat ? "Editar materia" : "Nueva materia"}
                        </h3>

                        <form onSubmit={handleSubmitMat} className="space-y-3">
                            <input
                                type="text"
                                placeholder="Nombre de la materia"
                                value={matForm.nombreMateria}
                                onChange={e => setMatForm({ nombreMateria: e.target.value })}
                                required
                                className="w-full border rounded px-3 py-2"
                            />

                            {matError && (
                                <p className="text-red-600 text-sm">{matError}</p>
                            )}

                            <button
                                type="submit"
                                className="w-full bg-blue-600 text-white py-2 rounded"
                            >
                                Guardar
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default DashboardEncargado;
