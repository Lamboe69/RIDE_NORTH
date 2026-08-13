function Freight() {
  return (
    <div>
      <h1>Freight Marketplace</h1>
      <p>Monitor freight jobs, quotes, and delivery status.</p>
      <table style={{ width: '100%', marginTop: '1rem', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid #ccc' }}>
            <th style={{ textAlign: 'left', padding: '8px' }}>Job ID</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Shipper</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Route</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Cargo</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Status</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Quote Range</th>
          </tr>
        </thead>
        <tbody>
          <tr style={{ borderBottom: '1px solid #eee' }}>
            <td style={{ padding: '8px' }}>#FREIGHT-001</td>
            <td style={{ padding: '8px' }}>Gulu Trader</td>
            <td style={{ padding: '8px' }}>Gulu → Lira</td>
            <td style={{ padding: '8px' }}>Agricultural</td>
            <td style={{ padding: '8px' }}>Open</td>
            <td style={{ padding: '8px' }}>UGX 50,000 - 80,000</td>
          </tr>
        </tbody>
      </table>
    </div>
  )
}

export default Freight
