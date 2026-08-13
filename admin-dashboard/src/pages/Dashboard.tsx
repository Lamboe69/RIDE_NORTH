import { Link } from 'react-router-dom'

function Dashboard() {
  return (
    <div>
      <h1>RideNorth Admin Dashboard</h1>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', marginTop: '2rem' }}>
        <div style={{ padding: '1rem', border: '1px solid #ccc', borderRadius: '8px' }}>
          <h3>Active Drivers</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>0</p>
        </div>
        <div style={{ padding: '1rem', border: '1px solid #ccc', borderRadius: '8px' }}>
          <h3>Active Rides</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>0</p>
        </div>
        <div style={{ padding: '1rem', border: '1px solid #ccc', borderRadius: '8px' }}>
          <h3>Today's Revenue</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>UGX 0</p>
        </div>
        <div style={{ padding: '1rem', border: '1px solid #ccc', borderRadius: '8px' }}>
          <h3>Pending Verifications</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>0</p>
        </div>
      </div>
      <nav style={{ marginTop: '2rem' }}>
        <Link to="/drivers" style={{ marginRight: '1rem' }}>Drivers</Link>
        <Link to="/rides" style={{ marginRight: '1rem' }}>Rides</Link>
        <Link to="/freight" style={{ marginRight: '1rem' }}>Freight</Link>
        <Link to="/disputes">Disputes</Link>
      </nav>
    </div>
  )
}

export default Dashboard
