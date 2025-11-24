import { jsx as _jsx, jsxs as _jsxs } from "react/jsx-runtime";
/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useRef, useState } from 'react';
import useAuth from '../../hooks/useAuth';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
const RegistrosPage = () => {
    const { user } = useAuth();
    const userId = user?.codigoUsuario ?? user?.idUsuario ?? '';
    const canFilter = /(?:INSTRUCTOR_NORMAL|INSTRUCTOR_REMUNERADO)/i.test(user?.rol ?? '');
    /* materias y materia seleccionada */
    const [materias, setMaterias] = useState([]);
    const [materiaSel, setMateriaSel] = useState('');
    /* registros aprobados filtrados */
    const [registros, setRegistros] = useState([]);
    const printableRef = useRef(null);
    // Datos fijos y rellenables
    const [info, setInfo] = useState({
        nombre: user?.nombre || '',
        carrera: 'Ingenieria Informatica',
        carnet: userId,
        telefono: '',
        proyecto: '',
        institucion: '',
        inicio: '',
    });
    /* ── obtener materias asignadas ── */
    useEffect(() => {
        if (!canFilter)
            return;
        const rawP = localStorage.getItem('usuarioXmateria');
        const rawM = localStorage.getItem('materias');
        if (!rawP || !rawM)
            return;
        const puente = JSON.parse(rawP);
        const catalog = JSON.parse(rawM);
        const ids = puente.filter(p => p.id_usuario === userId).map(p => p.id_materia);
        setMaterias(catalog.filter(m => ids.includes(m.id)));
        setMateriaSel(ids[0] || '');
    }, [canFilter, userId]);
    /* ── cargar registros aprobados ── */
    useEffect(() => {
        const raw = localStorage.getItem('registros');
        if (!raw) {
            setRegistros([]);
            return;
        }
        const all = JSON.parse(raw);
        setRegistros(all.filter(r => r.estudianteId === userId &&
            r.estado === 'APROBADO' &&
            (!materiaSel || r.materia === materiaSel)));
    }, [userId, materiaSel]);
    /* ── Generar PDF con márgenes y alta resolución ── */
    const downloadPdf = async () => {
        if (!printableRef.current)
            return;
        const canvas = await html2canvas(printableRef.current, { scale: 2 });
        const pdf = new jsPDF('l', 'pt', 'a4');
        const margin = 20;
        const pageWidth = pdf.internal.pageSize.getWidth();
        const usableWidth = pageWidth - margin * 2;
        const height = (canvas.height * usableWidth) / canvas.width;
        pdf.addImage(canvas.toDataURL('image/png'), 'PNG', margin, margin, usableWidth, height);
        pdf.save(`Registro_Asistencia_${info.carnet}_${Date.now()}.pdf`);
    };
    return (_jsxs("div", { className: "space-y-8 p-6 max-w-5xl mx-auto", children: [_jsxs("header", { className: "flex flex-col md:flex-row md:items-center md:justify-between gap-4", children: [_jsx("h1", { className: "text-3xl font-bold text-[#003c71]", children: "Mis Registros Aprobados" }), _jsxs("div", { className: "flex items-center gap-3", children: [canFilter && (_jsxs("select", { value: materiaSel, onChange: e => setMateriaSel(e.target.value), className: "border border-gray-300 rounded px-3 py-2 bg-white", children: [_jsx("option", { value: "", children: "Todas las Materias" }), materias.map(m => (_jsx("option", { value: m.id, children: m.nombre }, m.id)))] })), _jsx("button", { onClick: downloadPdf, className: "bg-[#003c71] hover:bg-[#002f59] text-white px-4 py-2 rounded flex items-center shadow", children: "Descargar PDF" })] })] }), canFilter && (_jsxs("section", { className: "bg-white p-5 rounded-xl shadow space-y-4", children: [_jsx("h2", { className: "text-xl font-semibold text-[#003c71]", children: "Informaci\u00F3n para Control de Asistencia" }), _jsx("div", { className: "grid grid-cols-1 md:grid-cols-2 gap-4", children: [
                            ['Carrera', info.carrera, true],
                            ['Carnet', info.carnet, true],
                            ['Teléfono', info.telefono, false],
                            ['Proyecto', info.proyecto, false],
                            ['Responsable', info.institucion, false],
                            ['Fecha inicio', info.inicio, false, 'date'],
                        ].map(([label, val, ro, type], i) => (_jsxs("div", { className: "flex flex-col", children: [_jsx("label", { className: "text-sm font-medium", children: label }), _jsx("input", { type: type || 'text', value: val, readOnly: ro, onChange: e => {
                                        const v = e.target.value;
                                        const key = (['carrera', 'carnet', 'telefono', 'proyecto', 'institucion', 'inicio'][i]);
                                        setInfo(info => ({ ...info, [key]: v }));
                                    }, className: `w-full border rounded px-3 py-2 ${ro ? 'bg-gray-100 cursor-not-allowed' : ''}` })] }, i))) })] })), _jsxs("div", { ref: printableRef, className: "bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden", children: [_jsxs("div", { className: "bg-gray-100 px-6 py-5 grid grid-cols-1 md:grid-cols-2 gap-6", children: [_jsxs("div", { className: "space-y-2", children: [_jsxs("p", { children: [_jsx("strong", { children: "Nombre:" }), " ", info.nombre] }), _jsxs("p", { children: [_jsx("strong", { children: "Carrera:" }), " ", info.carrera] }), _jsxs("p", { children: [_jsx("strong", { children: "Carnet:" }), " ", info.carnet] }), _jsxs("p", { children: [_jsx("strong", { children: "Tel\u00E9fono:" }), " ", info.telefono || '-'] })] }), _jsxs("div", { className: "space-y-2", children: [_jsxs("p", { children: [_jsx("strong", { children: "Proyecto:" }), " ", info.proyecto || '-'] }), _jsxs("p", { children: [_jsx("strong", { children: "Responsable:" }), " ", info.institucion || '-'] }), _jsxs("p", { children: [_jsx("strong", { children: "Fecha inicio:" }), " ", info.inicio || '-'] }), _jsxs("p", { children: [_jsx("strong", { children: "Materia:" }), " ", materias.find(m => m.id === materiaSel)?.nombre || '—'] })] })] }), _jsx("div", { className: "p-6 overflow-x-auto", children: _jsxs("table", { className: "min-w-full border-collapse border border-gray-300", children: [_jsx("thead", { children: _jsx("tr", { className: "bg-white", children: [
                                            'Fecha',
                                            'Hora inicio',
                                            'Hora fin',
                                            'Actividad',
                                            'Aula',
                                            'Horas efectivas',
                                            'Firma Encargado'
                                        ].map(col => (_jsx("th", { className: "border border-gray-300 px-4 py-2 text-left text-sm font-medium text-gray-700", children: col }, col))) }) }), _jsx("tbody", { children: registros.length ? registros.map(r => (_jsxs("tr", { className: "bg-white hover:bg-gray-50", children: [_jsx("td", { className: "border border-gray-300 px-4 py-2 text-sm", children: r.fecha }), _jsx("td", { className: "border border-gray-300 px-4 py-2 text-sm", children: r.horaInicio }), _jsx("td", { className: "border border-gray-300 px-4 py-2 text-sm", children: r.horaFin }), _jsx("td", { className: "border border-gray-300 px-4 py-2 text-sm", children: r.actividad }), _jsx("td", { className: "border border-gray-300 px-4 py-2 text-sm", children: r.aula }), _jsx("td", { className: "border border-gray-300 px-4 py-2 text-sm", children: r.horasEfectivas }), _jsx("td", { className: "border border-gray-300 px-4 py-2 text-sm", children: "________________" })] }, r.id))) : (_jsx("tr", { children: _jsx("td", { colSpan: 7, className: "border border-gray-300 px-4 py-6 text-center text-gray-500", children: "No hay registros aprobados." }) })) })] }) })] })] }));
};
export default RegistrosPage;
