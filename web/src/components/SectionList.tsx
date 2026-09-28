import { useEffect, useState } from 'react'
import { getMySections } from '../services/api'
import type { Section } from '../services/api'

interface Props {
  onSelect: (section: Section) => void
}

export default function SectionList({ onSelect }: Props) {
  const [sections, setSections] = useState<Section[]>([])
  const [error, setError] = useState('')

  useEffect(() => {
    getMySections().then(setSections).catch(() => setError('No se pudieron cargar tus secciones'))
  }, [])

  if (error) return <p className="error">{error}</p>

  return (
    <div>
      <h2>Mis secciones</h2>
      <ul className="course-list">
        {sections.map((s) => (
          <li key={s.id}>
            <button className="link" onClick={() => onSelect(s)}>
              {s.descripcion}
            </button>
          </li>
        ))}
      </ul>
    </div>
  )
}