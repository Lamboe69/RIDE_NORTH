import Layout from '../components/Layout'
import { StatCard } from '../components/ui'
import { IconCar, IconMoney, IconTrending, IconUsers } from '../components/icons'

const rides = [
  { id: '#TRIP-042', rider: '+256 706 000 000', driver: '+256 707 000 000', vehicle: 'Boda Boda', route: 'Citi Link → Market Square', status: 'Ongoing', tone: 'blue' as const, fare: '—' },
  { id: '#TRIP-041', rider: 'Okello S.', driver: 'Lanyero D.', vehicle: 'Boda Boda', route: 'Gulu City Centre → Laroo', status: 'Completed', tone: 'green' as const, fare: 'UGX 5,000' },
  { id: '#TRIP-040', rider: 'Aciro G.', driver: 'Odongo P.', vehicle: 'Tuk Tuk', route: 'Senior Quarters → Market', status: 'Completed', tone: 'green' as const, fare: 'UGX 7,500' },
  { id: '#TRIP-039', rider: 'Acen S.', driver: 'John D.', vehicle: 'Private Car', route: 'Hotel Ulan → Gulu Airport', status: 'Cancelled', tone: 'red' as const, fare: '—' },
  { id: '#TRIP-038', rider: 'Lanyero D.', driver: 'Aciro G.', vehicle: 'Boda Boda', route: 'UniGulu → Citi Link', status: 'Completed', tone: 'green' as const, fare: 'UGX 4,000' },
  { id: '#TRIP-037', rider: 'Opiyo T.', driver: 'Okello M.', vehicle: 'Boda Boda', route: 'Laroo Pece → Gulu Stadium', status: 'Disputed', tone: 'amber' as const, fare: 'UGX 6,000' },
]

function Rides() {
  return (
    <Layout>
      <div className="page-header">
        <h1>Ride Management</h1>
        <p>Monitor active rides, view trip history, and handle disputes.</p>
      </div>

      <div className="kpi-grid">
        <StatCard label="Rides Today" value="412" icon={<IconCar size={20} />} tone="green" foot={<span><span className="up">▲ 8%</span> vs yesterday</span>} />
        <StatCard label="Active Now" value="36" icon={<IconUsers size={20} />} tone="blue" foot={<span>riders currently moving</span>} />
        <StatCard label="Gross Revenue" value="UGX 2.1M" icon={<IconMoney size={20} />} tone="amber" foot={<span>today, before commission</span>} />
        <StatCard label="Completion Rate" value="94.2%" icon={<IconTrending size={20} />} tone="purple" foot={<span><span className="up">▲ 1.4%</span> this week</span>} />
      </div>

      <div className="card">
        <div className="card-header">
          <h3>All Rides</h3>
          <div style={{ display: 'flex', gap: '8px' }}>
            <button className="btn btn-outline btn-sm">Filter</button>
            <button className="btn btn-outline btn-sm">Export</button>
          </div>
        </div>
        <div className="table-wrap">
          <table className="data-table">
            <thead>
              <tr>
                <th>Trip ID</th>
                <th>Rider</th>
                <th>Driver</th>
                <th>Vehicle</th>
                <th>Route</th>
                <th>Status</th>
                <th>Fare</th>
              </tr>
            </thead>
            <tbody>
              {rides.map((r, i) => (
                <tr key={i}>
                  <td className="cell-main">{r.id}</td>
                  <td>{r.rider}</td>
                  <td>{r.driver}</td>
                  <td>{r.vehicle}</td>
                  <td>
                    <div className="cell-main">{r.route.split(' → ')[0]}</div>
                    <div className="cell-sub">→ {r.route.split(' → ')[1]}</div>
                  </td>
                  <td><span className={`badge ${r.tone}`}>{r.status}</span></td>
                  <td className="cell-main">{r.fare}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </Layout>
  )
}

export default Rides
