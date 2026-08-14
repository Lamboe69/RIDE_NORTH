import Layout from '../components/Layout'
import { StatCard } from '../components/ui'
import { IconAlert, IconCheck, IconTrending, IconUsers } from '../components/icons'

const disputes = [
  { id: '#DISP-010', type: 'Safety', reportedBy: 'Rider', trip: '#TRIP-037', summary: 'Rider reported rough driving on Laroo Pece route', status: 'Escalated', tone: 'red' as const, action: 'Resolve' },
  { id: '#DISP-009', type: 'Fare', reportedBy: 'Driver', trip: '#TRIP-033', summary: 'Driver says rider left without completing payment', status: 'Open', tone: 'amber' as const, action: 'Review' },
  { id: '#DISP-008', type: 'Damage', reportedBy: 'Driver', trip: '#TRIP-028', summary: 'Driver claims cargo damage during pickup', status: 'Open', tone: 'amber' as const, action: 'Review' },
  { id: '#DISP-007', type: 'Safety', reportedBy: 'Rider', trip: '#TRIP-021', summary: 'Rider reported missed pickup and long wait', status: 'Resolved', tone: 'green' as const, action: 'View' },
  { id: '#DISP-006', type: 'Payment', reportedBy: 'Shipper', trip: '#FREIGHT-008', summary: 'Escrow release disputed after delayed delivery', status: 'Resolved', tone: 'green' as const, action: 'View' },
  { id: '#DISP-005', type: 'Safety', reportedBy: 'Rider', trip: '#TRIP-015', summary: 'Minor accident reported, no injuries', status: 'Resolved', tone: 'green' as const, action: 'View' },
]

function Disputes() {
  return (
    <Layout>
      <div className="page-header">
        <h1>Dispute Resolution</h1>
        <p>Manage reported incidents, safety flags, and trip disputes.</p>
      </div>

      <div className="kpi-grid">
        <StatCard label="Open Disputes" value="3" icon={<IconAlert size={20} />} tone="red" foot={<span>need your attention</span>} />
        <StatCard label="Resolved This Week" value="11" icon={<IconCheck size={20} />} tone="green" foot={<span><span className="up">▲ 2</span> vs last week</span>} />
        <StatCard label="Avg Resolution Time" value="6.4h" icon={<IconTrending size={20} />} tone="purple" foot={<span>target is under 24h</span>} />
        <StatCard label="Drivers Flagged" value="4" icon={<IconUsers size={20} />} tone="amber" foot={<span>repeated low ratings</span>} />
      </div>

      <div className="card">
        <div className="card-header">
          <h3>Dispute Queue</h3>
          <div style={{ display: 'flex', gap: '8px' }}>
            <button className="btn btn-outline btn-sm">Filter</button>
            <button className="btn btn-outline btn-sm">Export</button>
          </div>
        </div>
        <div className="table-wrap">
          <table className="data-table">
            <thead>
              <tr>
                <th>Dispute ID</th>
                <th>Type</th>
                <th>Reported By</th>
                <th>Trip</th>
                <th>Summary</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {disputes.map((d, i) => (
                <tr key={i}>
                  <td className="cell-main">{d.id}</td>
                  <td><span className={`badge ${d.type === 'Safety' ? 'red' : 'gray'}`}>{d.type}</span></td>
                  <td>{d.reportedBy}</td>
                  <td>{d.trip}</td>
                  <td>{d.summary}</td>
                  <td><span className={`badge ${d.tone}`}>{d.status}</span></td>
                  <td>
                    <button className="btn btn-outline btn-sm">{d.action}</button>
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

export default Disputes
