import type { User } from '../types/models'
import { postData, getData } from './http'

export interface AuthResponse {
  token: string
  user: User
}

export function register(payload: { username: string; password: string; email?: string; nickname?: string }) {
  return postData<AuthResponse>('/api/auth/register', payload)
}

export function login(payload: { username: string; password: string }) {
  return postData<AuthResponse>('/api/auth/login', payload)
}

export function me() {
  return getData<User>('/api/auth/me')
}

