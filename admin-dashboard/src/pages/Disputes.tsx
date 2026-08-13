function Disputes() {
  return (
    <div>
      <h1>Dispute Resolution</h1>
      <p>Manage reported incidents, safety flags, and trip disputes.</p>
      <table style={{ width: '100%', marginTop: '1rem', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid #ccc' }}>
            <th style={{ textAlign: 'left', padding: '8px' }}>Dispute ID</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Type</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Reported By</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Status</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr style={{ borderBottom: '1px solid #eee' }}>
            <td style={{ padding: '8px' }}>#DISP-001</td>
            <td style={{ padding: '8px' }}>Safety</td>
            <td style={{ padding: '8px' }}>Rider</td>
            <td style={{ padding: '8px' }}>Open</td>
            <td style={{ padding: '8px' }}>
              <button>Review</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  )
}

export default Disputes
