import type { Paper, Question, SubmitPaperResponse } from '../types/models'
import { getData, postData } from './http'

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

export function getPapers(params?: {
  current?: number
  size?: number
  year?: number
  month?: number
  paperNo?: number
  type?: string
  difficulty?: string
}) {
  return getData<PageResult<Paper>>('/api/papers', params)
}

export function getPaper(id: number) {
  return getData<Paper>(`/api/papers/${id}`)
}

export function getQuestions(id: number, mode: 'practice' | 'exam' = 'practice') {
  return getData<Question[]>(`/api/papers/${id}/questions`, { mode })
}

export function submitPaper(id: number, payload: { durationSeconds?: number; answers: Record<string, string> }) {
  return postData<SubmitPaperResponse>(`/api/papers/${id}/submit`, payload)
}

