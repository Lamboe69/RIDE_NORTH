import Layout from '../components/Layout'
import { StatCard } from '../components/ui'
import { IconUsers, IconCheck, IconCar, IconTruck } from '../components/icons'

const drivers = [
  { name: 'John Doe', phone: '+256 700 000 000', vehicle: 'Boda Boda', plate: 'UAF 123L', trips: 412, rating: 4.8, badge: 'Pending', tone: 'amber' as const },
  { name: 'Aciro Grace', phone: '+256 701 111 111', vehicle: 'Tuk Tuk', plate: 'UGL 456A', trips: 1289, rating: 4.9, badge: 'Approved', tone: 'green' as const },
  { name: 'Okello Michael', phone: '+256 702 222 222', vehicle: 'Boda Boda', plate: 'UBA 789D', trips: 18, rating: 4.5, badge: 'Pending', tone: 'amber' as const },
  { name: 'Acen Sarah', phone: '+256 703 333 333', vehicle: 'Private Car', plate: 'UAH 321K', trips: 0, rating: 0, badge: 'Rejected', tone: 'red' as const },
  { name: 'Lanyero Dora', phone: '+256 704 444 444', vehicle: 'Truck', plate: 'UAP 654B', trips: 267, rating: 4.7, badge: 'Approved', tone: 'green' as const },
  { name: 'Odongo Peter', phone: '+256 705 555 555', vehicle: 'Lorry', plate: 'UAR 987C', trips: 94, rating: 4.6, badge: 'Approved', tone: 'green' as const },
]

function Drivers() {
  return (
    <Layout>
      <div className="page-header">
        <h1>Driver Management</h1>
        <p>Verify driver documents, manage KYC status, and monitor fleet activity.</p>
      </div>

      <div className="kpi-grid">
        <StatCard label="Total Drivers" value="128" icon={<IconUsers size={20} />} tone="green" foot={<span>across 3 vehicle classes</span>} />
        <StatCard label="Pending Verification" value="14" icon={<IconCheck size={20} />} tone="amber" foot={<span>awaiting document review</span>} />
        <StatCard label="Active Today" value="96" icon={<IconCar size={20} />} tone="blue" foot={<span>currently online</span>} />
        <StatCard label="Freight Operators" value="32" icon={<IconTruck size={20} />} tone="purple" foot={<span>trucks, lorries & tractors</span>} />
      </div>

      <div className="card">
        <div className="card-header">
          <h3>All Drivers</h3>
          <div style={{ display: 'flex', gap: '8px' }}>
            <button className="btn btn-outline btn-sm">Export</button>
            <button className="btn btn-green btn-sm">Invite Driver</button>
          </div>
        </div>
        <div className="table-wrap">
          <table className="data-table">
            <thead>
              <tr>
                <th>Driver</th>
                <th>Phone</th>
                <th>Vehicle</th>
                <th>Trips</th>
                <th>Rating</th>
                <th>KYC Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {drivers.map((d, i) => (
                <tr key={i}>
                  <td>
                    <div className="avatar-cell">
                      <div className="avatar">{d.name.split(' ').map((w) => w[0]).join('').slice(0, 2)}</div>
                      <div>
                        <div className="cell-main">{d.name}</div>
                        <div className="cell-sub">{d.plate}</div>
                      </div>
                    </div>
                  </td>
                  <td>{d.phone}</td>
                  <td className="cell-main">{d.vehicle}</td>
                  <td>{d.trips.toLocaleString()}</td>
                  <td>{d.rating > 0 ? `★ ${d.rating}` : '—'}</td>
                  <td><span className={`badge ${d.tone}`}>{d.badge}</span></td>
                  <td>
                    <div style={{ display: 'flex', gap: '6px' }}>
                      <button className="btn btn-outline btn-sm">Docs</button>
                      {d.badge === 'Pending' && <button className="btn btn-green btn-sm">Approve</button>}
                      {d.badge === 'Approved' && <button className="btn btn-danger btn-sm">Suspend</button>}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </Layout>
  )
}

export default Drivers
