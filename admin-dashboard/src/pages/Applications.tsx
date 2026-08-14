import { useCallback, useEffect, useState } from 'react'
import Layout from '../components/Layout'
import { StatCard } from '../components/ui'
import { IconUsers, IconCheck, IconX } from '../components/icons'
import { fetchApplications, approveApplication, rejectApplication, type DriverApplication } from '../api'

const vehicleIcon: Record<string, string> = {
  BODA: '🏍️',
  TUKTUK: '🛺',
  CAR: '🚗',
  TRUCK: '🚚',
  LORRY: '🚛',
}

function formatDate(iso: string | null) {
  if (!iso) return '—'
  try {
    return new Date(iso).toLocaleDateString('en-UG', { day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit' })
  } catch {
    return iso
  }
}

function Applications() {
  const [items, setItems] = useState<DriverApplication[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [busyId, setBusyId] = useState<string | null>(null)
  const [rejecting, setRejecting] = useState<DriverApplication | null>(null)
  const [reason, setReason] = useState('')

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      setItems(await fetchApplications())
    } catch (e) {
      setError('Could not load applications. Is the backend running on :8080?')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    load()
  }, [load])

  const handleApprove = async (app: DriverApplication) => {
    setBusyId(app.id)
    try {
      const updated = await approveApplication(app.id)
      setItems((prev) => prev.map((p) => (p.id === app.id ? updated : p)))
    } catch {
      setError('Approval failed. Check the server logs.')
    } finally {
      setBusyId(null)
    }
  }

  const handleReject = async () => {
    if (!rejecting) return
    setBusyId(rejecting.id)
    try {
      const updated = await rejectApplication(rejecting.id, reason.trim() || 'Documents did not meet verification requirements')
      setItems((prev) => prev.map((p) => (p.id === rejecting.id ? updated : p)))
      setRejecting(null)
      setReason('')
    } catch {
      setError('Rejection failed. Check the server logs.')
    } finally {
      setBusyId(null)
    }
  }

  const pending = items.filter((i) => i.status === 'PENDING').length
  const approved = items.filter((i) => i.status === 'APPROVED').length
  const rejected = items.filter((i) => i.status === 'REJECTED').length

  return (
    <Layout>
      <div className="page-header">
        <h1>Driver Applications</h1>
        <p>Prospective drivers apply in the app, you approve or reject, and their login details go out by SMS.</p>
      </div>

      <div className="kpi-grid">
        <StatCard label="Pending Review" value={String(pending)} icon={<IconUsers size={20} />} tone="amber" foot={<span>awaiting verification</span>} />
        <StatCard label="Approved" value={String(approved)} icon={<IconCheck size={20} />} tone="green" foot={<span>login details sent by SMS</span>} />
        <StatCard label="Rejected" value={String(rejected)} icon={<IconX size={20} />} tone="red" foot={<span>notified by SMS</span>} />
      </div>

      <div className="card">
        <div className="card-header">
          <h3>All Applications</h3>
          <button className="btn btn-outline btn-sm" onClick={load} disabled={loading}>
            {loading ? 'Loading...' : 'Refresh'}
          </button>
        </div>

        {error ? (
          <div className="empty-state">
            <p>{error}</p>
          </div>
        ) : items.length === 0 && !loading ? (
          <div className="empty-state">
            <p>No applications yet. Drivers can apply from the RideNorth Driver app.</p>
          </div>
        ) : (
          <div className="table-wrap">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Applicant</th>
                  <th>Phone</th>
                  <th>Vehicle</th>
                  <th>Permit / NIN</th>
                  <th>Submitted</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {items.map((app) => (
                  <tr key={app.id}>
                    <td>
                      <div className="avatar-cell">
                        <div className="avatar">{app.fullName.split(' ').map((w) => w[0]).join('').slice(0, 2)}</div>
                        <div>
                          <div className="cell-main">{app.fullName}</div>
                          <div className="cell-sub">{app.applicationRef}</div>
                        </div>
                      </div>
                    </td>
                    <td>+256 {app.phoneNumber}</td>
                    <td>
                      <div className="cell-main">
                        {vehicleIcon[app.vehicleType] ?? '🚙'} {app.vehicleType}
                      </div>
                      <div className="cell-sub">
                        {app.plateNumber} · {app.capacity}p {app.make ? `· ${app.make} ${app.model ?? ''}`.trim() : ''}
                      </div>
                    </td>
                    <td>
                      <div className="cell-main">{app.licenseNumber}</div>
                      <div className="cell-sub">{app.ninNumber ?? 'No NIN'}</div>
                    </td>
                    <td>{formatDate(app.submittedAt)}</td>
                    <td>
                      <span className={`badge ${app.status === 'PENDING' ? 'amber' : app.status === 'APPROVED' ? 'green' : 'red'}`}>
                        {app.status}
                      </span>
                      {app.rejectionReason && <div className="cell-sub">{app.rejectionReason}</div>}
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: '6px' }}>
                        {app.status === 'PENDING' ? (
                          <>
                            <button className="btn btn-green btn-sm" disabled={busyId === app.id} onClick={() => handleApprove(app)}>
                              {busyId === app.id ? '...' : 'Approve'}
                            </button>
                            <button className="btn btn-danger btn-sm" disabled={busyId === app.id} onClick={() => setRejecting(app)}>
                              Reject
                            </button>
                          </>
                        ) : (
                          <span className="badge gray">Reviewed</span>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {rejecting && (
        <div className="modal-backdrop" onClick={() => setRejecting(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Reject {rejecting.fullName}?</h3>
            <p>The applicant will receive an SMS with this reason.</p>
            <textarea
              className="input"
              rows={3}
              value={reason}
              placeholder="Reason (optional) - e.g. Driving permit not verified"
              onChange={(e) => setReason(e.target.value)}
            />
            <div className="modal-actions">
              <button className="btn btn-outline btn-sm" onClick={() => setRejecting(null)}>
                Cancel
              </button>
              <button className="btn btn-danger btn-sm" disabled={busyId === rejecting.id} onClick={handleReject}>
                {busyId === rejecting.id ? 'Rejecting...' : 'Confirm Reject'}
              </button>
            </div>
          </div>
        </div>
      )}
    </Layout>
  )
}

export default Applications
