import axios from 'axios'

export type ApplicationStatus = 'PENDING' | 'APPROVED' | 'REJECTED'

export interface DriverApplication {
  id: string
  applicationRef: string
  phoneNumber: string
  fullName: string
  ninNumber: string | null
  licenseNumber: string
  vehicleType: string
  plateNumber: string
  make: string | null
  model: string | null
  year: string | null
  capacity: number
  documents: string | null
  status: ApplicationStatus
  rejectionReason: string | null
  submittedAt: string | null
  reviewedAt: string | null
}

export const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

export async function fetchApplications(): Promise<DriverApplication[]> {
  const res = await api.get<DriverApplication[]>('/admin/driver-applications')
  return res.data
}

export async function approveApplication(id: string): Promise<DriverApplication> {
  const res = await api.post<DriverApplication>(`/admin/driver-applications/${id}/approve`)
  return res.data
}

export async function rejectApplication(id: string, reason: string): Promise<DriverApplication> {
  const res = await api.post<DriverApplication>(`/admin/driver-applications/${id}/reject`, { rejectionReason: reason })
  return res.data
}
