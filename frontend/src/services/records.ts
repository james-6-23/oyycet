import type { PracticeRecord, PracticeStats } from '../types/models'
import { getData } from './http'

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

export function getMyRecords(params?: { current?: number; size?: number }) {
  return getData<PageResult<PracticeRecord>>('/api/me/practice-records', params)
}

export function getMyStats() {
  return getData<PracticeStats>('/api/me/practice-records/stats')
}

