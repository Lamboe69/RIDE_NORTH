function Drivers() {
  return (
    <div>
      <h1>Driver Management</h1>
      <p>Verify driver documents, manage KYC status, and monitor fleet activity.</p>
      <table style={{ width: '100%', marginTop: '1rem', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid #ccc' }}>
            <th style={{ textAlign: 'left', padding: '8px' }}>Name</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Phone</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Vehicle</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>KYC Status</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr style={{ borderBottom: '1px solid #eee' }}>
            <td style={{ padding: '8px' }}>John Doe</td>
            <td style={{ padding: '8px' }}>+256 700 000 000</td>
            <td style={{ padding: '8px' }}>Boda Boda</td>
            <td style={{ padding: '8px' }}>Pending</td>
            <td style={{ padding: '8px' }}>
              <button style={{ marginRight: '4px' }}>Approve</button>
              <button>Reject</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  )
}

export default Drivers
