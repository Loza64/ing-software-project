import { jsx as _jsx, jsxs as _jsxs } from "react/jsx-runtime";
import { useState, useEffect, useMemo } from 'react';
import { CheckCircle, XCircle } from 'lucide-react';
import useAuth from '../../hooks/useAuth';
const ITEMS_PER_PAGE = 15;
const ValidacionesPage = () => {
    const { user } = useAuth();
    const [registros, setRegistros] = useState([]);
    const [usuarios] = useState(() => JSON.parse(localStorage.getItem('usuarios') || '[]'));
    const [materias] = useState(() => JSON.parse(localStorage.getItem('materias') || '[]'));
    const [searchCodigo, setSearchCodigo] = useState('');
    const [page, setPage] = useState(1);
    useEffect(() => {
        const data = localStorage.getItem('registros');
        if (data)
            setRegistros(JSON.parse(data));
    }, []);
    const getCodigoEstudiante = (r) => {
        const u = usuarios.find(u => u.idUsuario === r.estudianteId);
        return u?.codigoUsuario ?? r.estudianteId;
    };
    const getNombreEstudiante = (r) => {
        const u = usuarios.find(u => u.idUsuario === r.estudianteId);
        return u ? `${u.nombre} ${u.apellido}` : r.estudianteId;
    };
    const getMateriaEstudiante = (r) => {
        if (r.materia) {
            const m = materias.find(m => m.idMateria === r.materia);
            if (m)
                return m.nombreMateria;
        }
        // fallback: materia asignada al usuario
        const u = usuarios.find(u => u.idUsuario === r.estudianteId);
        if (u?.materiaId) {
            const m = materias.find(m => m.idMateria === u.materiaId);
            if (m)
                return m.nombreMateria;
        }
        return '—';
    };
    const actualizarEstado = (registroId, nuevo) => {
        const nuevos = registros.map(r => r.id === registroId ? { ...r, estado: nuevo } : r);
        setRegistros(nuevos);
        localStorage.setItem('registros', JSON.stringify(nuevos));
    };
    const registrosFiltrados = useMemo(() => {
        const term = searchCodigo.trim().toLowerCase();
        return registros.filter(r => r.estado === 'PENDIENTE' &&
            getCodigoEstudiante(r).toLowerCase().includes(term));
    }, [registros, searchCodigo]);
    const pageCount = Math.max(1, Math.ceil(registrosFiltrados.length / ITEMS_PER_PAGE));
    const paginated = useMemo(() => registrosFiltrados.slice((page - 1) * ITEMS_PER_PAGE, page * ITEMS_PER_PAGE), [registrosFiltrados, page]);
    return (_jsxs("div", { className: "space-y-6 p-4", children: [_jsxs("div", { className: "flex justify-between items-center", children: [_jsx("h2", { className: "text-2xl font-bold text-[#003c71]", children: "Validaciones de Registros" }), _jsxs("span", { className: "text-gray-600", children: ["Usuario: ", _jsx("strong", { children: user?.nombre })] })] }), _jsxs("div", { className: "flex items-center gap-4", children: [_jsx("label", { htmlFor: "searchCodigo", className: "font-medium", children: "Buscar por c\u00F3digo:" }), _jsx("input", { id: "searchCodigo", type: "text", placeholder: "Escribe el c\u00F3digo...", className: "border rounded px-3 py-1", value: searchCodigo, onChange: e => { setSearchCodigo(e.target.value); setPage(1); } })] }), _jsx("div", { className: "overflow-x-auto", children: _jsxs("table", { className: "min-w-full bg-white rounded shadow divide-y divide-gray-200", children: [_jsx("thead", { children: _jsxs("tr", { className: "bg-gray-100 text-gray-700 text-left", children: [_jsx("th", { className: "px-4 py-2", children: "C\u00F3digo" }), _jsx("th", { className: "px-4 py-2", children: "Estudiante" }), _jsx("th", { className: "px-4 py-2", children: "Materia" }), _jsx("th", { className: "px-4 py-2", children: "Fecha" }), _jsx("th", { className: "px-4 py-2", children: "Hora Inicio" }), _jsx("th", { className: "px-4 py-2", children: "Hora Fin" }), _jsx("th", { className: "px-4 py-2", children: "Actividad" }), _jsx("th", { className: "px-4 py-2", children: "Aula" }), _jsx("th", { className: "px-4 py-2", children: "Horas" }), _jsx("th", { className: "px-4 py-2", children: "Acciones" })] }) }), _jsxs("tbody", { className: "divide-y divide-gray-100", children: [paginated.map(r => (_jsxs("tr", { className: "hover:bg-gray-50", children: [_jsx("td", { className: "px-4 py-2", children: getCodigoEstudiante(r) }), _jsx("td", { className: "px-4 py-2", children: getNombreEstudiante(r) }), _jsx("td", { className: "px-4 py-2", children: getMateriaEstudiante(r) }), _jsx("td", { className: "px-4 py-2", children: r.fecha }), _jsx("td", { className: "px-4 py-2", children: r.horaInicio }), _jsx("td", { className: "px-4 py-2", children: r.horaFin }), _jsx("td", { className: "px-4 py-2", children: r.actividad }), _jsx("td", { className: "px-4 py-2", children: r.aula }), _jsx("td", { className: "px-4 py-2", children: r.horasEfectivas }), _jsxs("td", { className: "px-4 py-2 space-x-2", children: [_jsx("button", { className: "text-green-600 hover:text-green-800", onClick: () => actualizarEstado(r.id, 'APROBADO'), children: _jsx(CheckCircle, { size: 18 }) }), _jsx("button", { className: "text-red-600 hover:text-red-800", onClick: () => actualizarEstado(r.id, 'RECHAZADO'), children: _jsx(XCircle, { size: 18 }) })] })] }, r.id))), paginated.length === 0 && (_jsx("tr", { children: _jsx("td", { colSpan: 10, className: "px-4 py-2 text-center text-gray-500", children: "No hay registros pendientes." }) }))] })] }) }), _jsxs("div", { className: "flex justify-between items-center mt-4", children: [_jsxs("span", { children: ["P\u00E1gina ", page, " de ", pageCount] }), _jsxs("div", { className: "flex gap-2", children: [_jsx("button", { onClick: () => setPage(p => Math.max(1, p - 1)), disabled: page === 1, className: "px-3 py-1 rounded bg-gray-100 hover:bg-gray-200 disabled:opacity-50", children: "Anterior" }), _jsx("button", { onClick: () => setPage(p => Math.min(pageCount, p + 1)), disabled: page === pageCount, className: "px-3 py-1 rounded bg-gray-100 hover:bg-gray-200 disabled:opacity-50", children: "Siguiente" })] })] })] }));
};
export default ValidacionesPage;
