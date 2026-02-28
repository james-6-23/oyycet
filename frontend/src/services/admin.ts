import { getData, http } from './http'
import type { ApiResult } from '../types/api'
import type { AdminDashboardStats, AdminUser, PageResult, Paper } from '../types/models'

export async function getAdminDashboardStats() {
  return getData<AdminDashboardStats>('/api/admin/dashboard/stats')
}

export async function getAdminPapers(params: {
  current?: number
  size?: number
  status?: string
  title?: string
}) {
  return getData<PageResult<Paper>>('/api/admin/papers', params)
}

export async function importPaperJson(file: File) {
  const formData = new FormData()
  formData.append('file', file)
  const res = await http.post<ApiResult<Paper>>('/api/admin/papers/import-json', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  if (res.data.code !== 0) throw new Error(res.data.message)
  return res.data.data
}

export async function publishPaper(id: number) {
  const res = await http.post<ApiResult<void>>(`/api/admin/papers/${id}/publish`)
  if (res.data.code !== 0) throw new Error(res.data.message)
}

export async function unpublishPaper(id: number) {
  const res = await http.post<ApiResult<void>>(`/api/admin/papers/${id}/unpublish`)
  if (res.data.code !== 0) throw new Error(res.data.message)
}

export async function deletePaper(id: number) {
  const res = await http.delete<ApiResult<void>>(`/api/admin/papers/${id}`)
  if (res.data.code !== 0) throw new Error(res.data.message)
}

export async function getAdminUsers(params: {
  current?: number
  size?: number
  username?: string
}) {
  return getData<PageResult<AdminUser>>('/api/admin/users', params)
}

export async function updateUserRole(id: number, role: string) {
  const res = await http.put<ApiResult<void>>(`/api/admin/users/${id}/role`, { role })
  if (res.data.code !== 0) throw new Error(res.data.message)
}

export async function updateUserStatus(id: number, status: number) {
  const res = await http.put<ApiResult<void>>(`/api/admin/users/${id}/status`, { status })
  if (res.data.code !== 0) throw new Error(res.data.message)
}
