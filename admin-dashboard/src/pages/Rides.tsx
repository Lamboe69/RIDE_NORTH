function Rides() {
  return (
    <div>
      <h1>Ride Management</h1>
      <p>Monitor active rides, view trip history, and handle disputes.</p>
      <table style={{ width: '100%', marginTop: '1rem', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid #ccc' }}>
            <th style={{ textAlign: 'left', padding: '8px' }}>Trip ID</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Rider</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Driver</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Status</th>
            <th style={{ textAlign: 'left', padding: '8px' }}>Fare</th>
          </tr>
        </thead>
        <tbody>
          <tr style={{ borderBottom: '1px solid #eee' }}>
            <td style={{ padding: '8px' }}>#TRIP-001</td>
            <td style={{ padding: '8px' }}>+256 701 000 000</td>
            <td style={{ padding: '8px' }}>+256 702 000 000</td>
            <td style={{ padding: '8px' }}>Completed</td>
            <td style={{ padding: '8px' }}>UGX 5,000</td>
          </tr>
        </tbody>
      </table>
    </div>
  )
}

export default Rides
