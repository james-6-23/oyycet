import axios from 'axios'
import type { ApiResult } from '../types/api'
import { useAuthStore } from '../stores/auth'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export const http = axios.create({
  baseURL: apiBaseUrl,
  timeout: 15000,
})

http.interceptors.request.use((config) => {
  const auth = useAuthStore()
  if (auth.token) {
    config.headers = config.headers ?? {}
    config.headers.Authorization = `Bearer ${auth.token}`
  }
  return config
})

export async function getData<T>(url: string, params?: any): Promise<T> {
  const res = await http.get<ApiResult<T>>(url, { params })
  if (res.data.code !== 0) throw new Error(res.data.message)
  return res.data.data
}

export async function postData<T>(url: string, body?: any): Promise<T> {
  const res = await http.post<ApiResult<T>>(url, body)
  if (res.data.code !== 0) throw new Error(res.data.message)
  return res.data.data
}

