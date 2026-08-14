import Layout from '../components/Layout'
import { StatCard } from '../components/ui'
import { IconUsers, IconCar, IconMoney, IconCheck, IconTruck, IconAlert } from '../components/icons'

const week = [
  { day: 'Mon', pct: 45 },
  { day: 'Tue', pct: 62 },
  { day: 'Wed', pct: 40 },
  { day: 'Thu', pct: 78 },
  { day: 'Fri', pct: 90 },
  { day: 'Sat', pct: 58 },
  { day: 'Sun', pct: 30 },
]

const activity = [
  { icon: IconUsers, tone: 'green', title: 'New driver application', sub: 'Okello Michael · Boda Boda', time: '4m ago' },
  { icon: IconCar, tone: 'blue', title: 'Ride completed', sub: 'TRIP-041 · Gulu City Centre', time: '12m ago' },
  { icon: IconTruck, tone: 'amber', title: 'Freight quote accepted', sub: 'FREIGHT-014 · Gulu → Lira', time: '34m ago' },
  { icon: IconAlert, tone: 'red', title: 'Dispute escalated', sub: 'DISP-009 · Safety flag', time: '1h ago' },
  { icon: IconCheck, tone: 'green', title: 'Driver verified', sub: 'Aciro Grace · Tuk Tuk', time: '2h ago' },
]

const driverRows = [
  { name: 'John Doe', phone: '+256 700 000 000', vehicle: 'Boda Boda', badge: 'Pending', tone: 'amber' as const },
  { name: 'Aciro Grace', phone: '+256 701 111 111', vehicle: 'Tuk Tuk', badge: 'Approved', tone: 'green' as const },
  { name: 'Okello Michael', phone: '+256 702 222 222', vehicle: 'Boda Boda', badge: 'Pending', tone: 'amber' as const },
  { name: 'Acen Sarah', phone: '+256 703 333 333', vehicle: 'Private Car', badge: 'Rejected', tone: 'red' as const },
  { name: 'Lanyero Dora', phone: '+256 704 444 444', vehicle: 'Truck', badge: 'Approved', tone: 'green' as const },
]

function Dashboard() {
  return (
    <Layout>
      <div className="page-header">
        <h1>Welcome back, Erick</h1>
        <p>Here is what is happening across RideNorth today.</p>
      </div>

      <div className="kpi-grid">
        <StatCard
          label="Active Drivers"
          value="128"
          icon={<IconUsers size={20} />}
          tone="green"
          foot={<span><span className="up">▲ 12</span> this week</span>}
        />
        <StatCard
          label="Active Rides"
          value="36"
          icon={<IconCar size={20} />}
          tone="blue"
          foot={<span><span className="up">▲ 8</span> this week</span>}
        />
        <StatCard
          label="Today's Revenue"
          value="UGX 1.2M"
          icon={<IconMoney size={20} />}
          tone="amber"
          foot={<span><span className="up">▲ 18%</span> vs yesterday</span>}
        />
        <StatCard
          label="Pending Verifications"
          value="14"
          icon={<IconCheck size={20} />}
          tone="red"
          foot={<span><span className="down">▼ 3</span> need attention</span>}
        />
      </div>

      <div className="grid-2">
        <div className="card">
          <div className="card-header">
            <h3>Weekly Trips</h3>
            <a href="#/rides">View all</a>
          </div>
          <div className="card-body">
            <div className="bar-chart">
              {week.map((d) => (
                <div className="bar-col" key={d.day}>
                  <div className="bar" style={{ height: `${d.pct}%` }} />
                  <span>{d.day}</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-header">
            <h3>Recent Activity</h3>
            <a href="#/disputes">More</a>
          </div>
          <div className="activity-list" style={{ padding: '4px 20px 14px' }}>
            {activity.map((a, i) => (
              <div className="activity-item" key={i}>
                <span className={`a-icon ${a.tone === 'green' ? 'green' : a.tone === 'blue' ? 'blue' : a.tone === 'amber' ? 'amber' : 'red'}`}>
                  <a.icon size={16} />
                </span>
                <div className="a-body">
                  <div className="a-title">{a.title}</div>
                  <div className="a-sub">{a.sub}</div>
                </div>
                <span className="a-time">{a.time}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="card" style={{ marginTop: '18px' }}>
        <div className="card-header">
          <h3>Driver Verification Queue</h3>
          <a href="#/drivers">Manage drivers</a>
        </div>
        <div className="table-wrap">
          <table className="data-table">
            <thead>
              <tr>
                <th>Driver</th>
                <th>Phone</th>
                <th>Vehicle</th>
                <th>KYC Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {driverRows.map((d, i) => (
                <tr key={i}>
                  <td>
                    <div className="avatar-cell">
                      <div className="avatar">{d.name.split(' ').map((w) => w[0]).join('').slice(0, 2)}</div>
                      <span className="cell-main">{d.name}</span>
                    </div>
                  </td>
                  <td>{d.phone}</td>
                  <td className="cell-main">{d.vehicle}</td>
                  <td>
                    <span className={`badge ${d.tone}`}>{d.badge}</span>
                  </td>
                  <td>
                    <button className="btn btn-outline btn-sm" style={{ marginRight: '6px' }}>Review</button>
                    <button className="btn btn-green btn-sm">Approve</button>
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

export default Dashboard
