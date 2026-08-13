import { Routes, Route } from 'react-router-dom'
import Dashboard from './pages/Dashboard'
import Drivers from './pages/Drivers'
import Rides from './pages/Rides'
import Freight from './pages/Freight'
import Disputes from './pages/Disputes'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/drivers" element={<Drivers />} />
      <Route path="/rides" element={<Rides />} />
      <Route path="/freight" element={<Freight />} />
      <Route path="/disputes" element={<Disputes />} />
    </Routes>
  )
}

export default App
