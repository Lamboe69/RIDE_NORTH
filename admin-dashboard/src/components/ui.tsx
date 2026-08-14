import type { ReactNode } from 'react'

export interface StatCardProps {
  label: string
  value: string
  foot?: ReactNode
  icon: ReactNode
  tone?: 'green' | 'amber' | 'blue' | 'red' | 'purple'
}

export function StatCard({ label, value, foot, icon, tone = 'green' }: StatCardProps) {
  return (
    <div className="stat-card">
      <div className="stat-top">
        <span className="stat-label">{label}</span>
        <span className={`stat-icon ${tone}`}>{icon}</span>
      </div>
      <div className="stat-value">{value}</div>
      {foot ? <div className="stat-foot">{foot}</div> : null}
    </div>
  )
}

export type BadgeTone = 'green' | 'amber' | 'red' | 'blue' | 'gray'

export function Badge({ tone = 'gray', children }: { tone?: BadgeTone; children: ReactNode }) {
  return <span className={`badge ${tone}`}>{children}</span>
}
