import { useState } from 'react'
import Attendance from './components/Attendance'
import CourseList from './components/CourseList'
import Login from './components/Login'
import { loggedName, logout } from './services/api'
import type { Course } from './services/api'

export default function App() {
  const [token] = useState(() => localStorage.getItem('edufast_token'))
  const [userName, setUserName] = useState(loggedName())
  const [course, setCourse] = useState<Course | null>(null)

  if (!token) {
    return (
      <div className="page">
        <Login
          onLogin={(name) => {
            setUserName(name)
            window.location.reload()
          }}
        />
      </div>
    )
  }

  function handleLogout() {
    logout()
    window.location.reload()
  }

  return (
    <div className="page">
      <header className="header">
        <strong>EduFast</strong>
        <span>
          {userName} · <button className="link" onClick={handleLogout}>Salir</button>
        </span>
      </header>

      {course ? (
        <Attendance course={course} onBack={() => setCourse(null)} />
      ) : (
        <CourseList onSelect={setCourse} />
      )}
    </div>
  )
}