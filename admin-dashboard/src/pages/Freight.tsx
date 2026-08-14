import Layout from '../components/Layout'
import { StatCard } from '../components/ui'
import { IconTruck, IconMoney, IconCheck, IconUsers } from '../components/icons'

const jobs = [
  { id: '#FREIGHT-015', shipper: 'Lango Farms Ltd', route: 'Lira → Kampala', cargo: 'Sesame · 4,500 kg', vehicle: 'Lorry', status: 'Awaiting Quotes', tone: 'amber' as const, quote: 'UGX 0' },
  { id: '#FREIGHT-014', shipper: 'Gulu Trader', route: 'Gulu → Lira', cargo: 'Agricultural · 2,000 kg', vehicle: 'Truck', status: 'In Transit', tone: 'blue' as const, quote: 'UGX 68,000' },
  { id: '#FREIGHT-013', shipper: 'Acholi Milling Co', route: 'Kitgum → Gulu', cargo: 'Groundnuts · 1,200 kg', vehicle: 'Pickup', status: 'Completed', tone: 'green' as const, quote: 'UGX 42,000' },
  { id: '#FREIGHT-012', shipper: 'Okidi Enterprises', route: 'Nebbi → Arua', cargo: 'Fishing supplies · 3,000 kg', vehicle: 'Truck', status: 'Quote Accepted', tone: 'green' as const, quote: 'UGX 88,000' },
  { id: '#FREIGHT-011', shipper: 'West Nile Traders', route: 'Arua → Elegu', cargo: 'Mixed goods · 5,800 kg', vehicle: 'Lorry', status: 'Cancelled', tone: 'red' as const, quote: '—' },
  { id: '#FREIGHT-010', shipper: 'Bungatira Co-op', route: 'Bungatira → Gulu', cargo: 'Shea nuts · 900 kg', vehicle: 'Tractor', status: 'Completed', tone: 'green' as const, quote: 'UGX 55,000' },
]

function Freight() {
  return (
    <Layout>
      <div className="page-header">
        <h1>Freight Marketplace</h1>
        <p>Monitor freight jobs, quotes, and delivery status.</p>
      </div>

      <div className="kpi-grid">
        <StatCard label="Open Jobs" value="23" icon={<IconTruck size={20} />} tone="amber" foot={<span>awaiting quotes</span>} />
        <StatCard label="In Transit" value="9" icon={<IconUsers size={20} />} tone="blue" foot={<span>cargo moving right now</span>} />
        <StatCard label="Freight Volume" value="42.6t" icon={<IconCheck size={20} />} tone="green" foot={<span>moved this month</span>} />
        <StatCard label="Freight Revenue" value="UGX 8.4M" icon={<IconMoney size={20} />} tone="purple" foot={<span>gross this month</span>} />
      </div>

      <div className="card">
        <div className="card-header">
          <h3>All Freight Jobs</h3>
          <div style={{ display: 'flex', gap: '8px' }}>
            <button className="btn btn-outline btn-sm">Filter</button>
            <button className="btn btn-green btn-sm">Post Job</button>
          </div>
        </div>
        <div className="table-wrap">
          <table className="data-table">
            <thead>
              <tr>
                <th>Job ID</th>
                <th>Shipper</th>
                <th>Route</th>
                <th>Cargo</th>
                <th>Vehicle</th>
                <th>Status</th>
                <th>Quote</th>
              </tr>
            </thead>
            <tbody>
              {jobs.map((j, i) => (
                <tr key={i}>
                  <td className="cell-main">{j.id}</td>
                  <td>{j.shipper}</td>
                  <td>
                    <div className="cell-main">{j.route.split(' → ')[0]}</div>
                    <div className="cell-sub">→ {j.route.split(' → ')[1]}</div>
                  </td>
                  <td>{j.cargo}</td>
                  <td>{j.vehicle}</td>
                  <td><span className={`badge ${j.tone}`}>{j.status}</span></td>
                  <td className="cell-main">{j.quote}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </Layout>
  )
}

export default Freight
