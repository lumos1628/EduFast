import { useEffect, useState } from 'react'
import { getMyCourses } from '../services/api'
import type { Course } from '../services/api'

interface Props {
  onSelect: (course: Course) => void
}

export default function CourseList({ onSelect }: Props) {
  const [courses, setCourses] = useState<Course[]>([])
  const [error, setError] = useState('')

  useEffect(() => {
    getMyCourses().then(setCourses).catch(() => setError('No se pudieron cargar tus cursos'))
  }, [])

  if (error) return <p className="error">{error}</p>

  return (
    <div>
      <h2>Tus cursos</h2>
      <div className="course-grid">
        {courses.map((c) => (
          <button key={c.id} className="course-card" onClick={() => onSelect(c)}>
            <span className="course-name">{c.name}</span>
            <span className="course-code">{c.code}</span>
          </button>
        ))}
      </div>
    </div>
  )
}