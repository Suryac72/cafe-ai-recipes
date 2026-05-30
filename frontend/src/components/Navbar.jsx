import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar" id="main-navbar">
      <div className="navbar__inner">
        <div className="navbar__brand">
          <span className="navbar__logo">🧑🍳</span>
          <span className="navbar__title">Cafe AI</span>
        </div>

        <div className="navbar__links">
          <NavLink
            to="/"
            end
            className={({ isActive }) =>
              `navbar__link ${isActive ? 'navbar__link--active' : ''}`
            }
            id="nav-generator"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/></svg>
            Generator
          </NavLink>
          <NavLink
            to="/saved"
            className={({ isActive }) =>
              `navbar__link ${isActive ? 'navbar__link--active' : ''}`
            }
            id="nav-saved"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
            Saved Recipes
          </NavLink>
        </div>

        <div className="navbar__user">
          {user?.pictureUrl && (
            <img
              src={user.pictureUrl}
              alt={user.name}
              className="navbar__avatar"
              referrerPolicy="no-referrer"
            />
          )}
          <span className="navbar__username">{user?.name}</span>
          <button className="navbar__logout" onClick={handleLogout} id="logout-btn">
            Sign Out
          </button>
        </div>
      </div>
    </nav>
  );
}
