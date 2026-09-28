import { useState } from 'react'
import Attendance from './components/Attendance'
import SectionList from './components/SectionList'
import Login from './components/Login'
import { loggedName, logout } from './services/api'
import type { Section } from './services/api'

export default function App() {
  const [token] = useState(() => localStorage.getItem('edufast_token'))
  const [userName, setUserName] = useState(loggedName())
  const [section, setSection] = useState<Section | null>(null)

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

      {section ? (
        <Attendance section={section} onBack={() => setSection(null)} />
      ) : (
        <SectionList onSelect={setSection} />
      )}
    </div>
  )
}