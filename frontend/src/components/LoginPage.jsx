import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { GoogleLogin } from '@react-oauth/google';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css';

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleGoogleSuccess = async (credentialResponse) => {
    try {
      setError(null);
      setIsLoading(true);
      await login(credentialResponse.credential);
      navigate('/');
    } catch (err) {
      setError('Login failed. Please try again.');
      setIsLoading(false);
    }
  };

  const handleGoogleError = () => {
    setError('Google Sign-In was cancelled or failed.');
  };

  return (
    <div className="login-page">
      <div className="login-page__glow login-page__glow--1" />
      <div className="login-page__glow login-page__glow--2" />

      <div className="login-card">
        <div className="login-card__icon">🧑🍳</div>
        <h1 className="login-card__title">Cafe AI</h1>
        <p className="login-card__subtitle">
          Turn surplus ingredients into creative menu items with AI
        </p>

        <div className="login-card__divider">
          <span>Sign in to continue</span>
        </div>

        <div className="login-card__google-btn" id="google-login-btn">
          {isLoading ? (
            <div className="login-loading">
              <div className="login-spinner" />
              <p className="login-loading__text">Signing in...</p>
            </div>
          ) : (
            <GoogleLogin
              onSuccess={handleGoogleSuccess}
              onError={handleGoogleError}
              theme="filled_black"
              size="large"
              width="320"
              text="signin_with"
              shape="pill"
            />
          )}
        </div>

        {error && (
          <div className="login-card__error">
            ⚠️ {error}
          </div>
        )}

        <p className="login-card__footer">
          Powered by Grok AI & Google Sign-In
        </p>
      </div>
    </div>
  );
}
