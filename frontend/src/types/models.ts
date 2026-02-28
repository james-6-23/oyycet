export interface User {
  id: number
  username: string
  email?: string
  nickname?: string
  role: 'STUDENT' | 'ADMIN'
}

export interface Paper {
  id: number
  title: string
  year?: number
  month?: number
  paperNo?: number
  difficulty?: string
  type?: string
  durationMin?: number
  status?: string
  attempts?: number
  description?: string
}

export interface Option {
  key: string
  content: string
}

export interface Question {
  id: number
  paperId: number
  type: string
  subType?: string
  questionNumber?: number
  score?: number
  content?: string
  options?: Option[]
  correctAnswer?: string
  explanation?: string
  passage?: string
  wordBank?: string
  audioUrl?: string
  audioStartTime?: number
  audioEndTime?: number
  passageGroup?: string
  directions?: string
  sortOrder?: number
}

export interface SubmitPaperResponse {
  recordId: number
  score: number
  correctCount: number
  totalCount: number
  durationSeconds: number
}

export interface PracticeRecord {
  id: number
  paperId: number
  paperTitle?: string
  paperYear?: number
  paperType?: string
  score: number
  durationSeconds: number
  correctCount: number
  totalCount: number
  finishTime: string
  createdAt?: string
}

export interface PracticeStats {
  totalPractices: number
  averageScore: number
  totalCorrect: number
  totalQuestions: number
  accuracy: number
  totalDuration: number
}

export interface AdminDashboardStats {
  totalUsers: number
  totalPapers: number
  totalPublished: number
  totalRecords: number
  todayRecords: number
}

export interface AdminUser {
  id: number
  username: string
  email?: string
  nickname?: string
  role: string
  status: number
  createTime: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

