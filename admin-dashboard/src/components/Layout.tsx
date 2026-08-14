import type { ReactNode } from 'react'
import { NavLink, useLocation } from 'react-router-dom'
import { IconGrid, IconUsers, IconClipboard, IconCar, IconTruck, IconAlert, IconBell, IconSearch } from './icons'

interface LayoutProps {
  children: ReactNode
}

const navItems = [
  { to: '/', label: 'Overview', icon: IconGrid },
  { to: '/drivers', label: 'Drivers', icon: IconUsers },
  { to: '/applications', label: 'Applications', icon: IconClipboard },
  { to: '/rides', label: 'Rides', icon: IconCar },
  { to: '/freight', label: 'Freight', icon: IconTruck },
  { to: '/disputes', label: 'Disputes', icon: IconAlert },
]

const titles: Record<string, string> = {
  '/': 'Operations Overview',
  '/drivers': 'Driver Management',
  '/applications': 'Driver Applications',
  '/rides': 'Ride Management',
  '/freight': 'Freight Marketplace',
  '/disputes': 'Dispute Resolution',
}

function Layout({ children }: LayoutProps) {
  const location = useLocation()
  const title = titles[location.pathname] ?? 'RideNorth'

  return (
    <div className="layout">
      <aside className="sidebar">
        <div className="sidebar-brand">
          <div className="sidebar-logo">RN</div>
          <div>
            <h1>RideNorth</h1>
            <small>Ops Console</small>
          </div>
        </div>

        <nav className="sidebar-nav">
          <div className="sidebar-label">Management</div>
          {navItems.map((item) => (
            <NavLink key={item.to} to={item.to} end={item.to === '/'} className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}>
              <item.icon size={18} />
              {item.label}
            </NavLink>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div className="avatar">OE</div>
          <div>
            <p>Ojok Erick</p>
            <small>Platform Admin</small>
          </div>
        </div>
      </aside>

      <div className="main">
        <header className="topbar">
          <h2>{title}</h2>
          <div className="topbar-actions">
            <div className="search-box">
              <IconSearch size={16} />
              <input placeholder="Search drivers, trips, disputes..." />
            </div>
            <button className="icon-btn" aria-label="Notifications">
              <IconBell size={17} />
              <span className="dot" />
            </button>
          </div>
        </header>
        <div className="page">{children}</div>
      </div>
    </div>
  )
}

export default Layout
