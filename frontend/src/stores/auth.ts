import { defineStore } from 'pinia'
import type { User } from '../types/models'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: null as User | null,
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
  },
  actions: {
    setAuth(token: string, user: User) {
      this.token = token
      this.user = user
      localStorage.setItem('token', token)
    },
    clear() {
      this.token = ''
      this.user = null
      localStorage.removeItem('token')
    },
    logout() {
      this.clear()
    },
  },
})

