<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NCard, NButton, NProgress, NSpace, NInput, NRadioGroup, NRadio, NModal } from 'naive-ui'
import { getQuestions, submitPaper } from '../services/papers'
import type { Question } from '../types/models'
import LoadingState from '../components/LoadingState.vue'
import ErrorState from '../components/ErrorState.vue'

const route = useRoute()
const router = useRouter()
const id = Number(route.params.id)

const loading = ref(false)
const error = ref('')
const submitting = ref(false)
const questions = ref<Question[]>([])
const answers = ref<Record<string, string>>({})
const currentIndex = ref(0)
const showResult = ref(false)
const result = ref<{ score: number; recordId: number } | null>(null)

const currentQuestion = computed(() => questions.value[currentIndex.value])
const progress = computed(() => ((currentIndex.value + 1) / questions.value.length) * 100)
const answeredCount = computed(() => Object.keys(answers.value).length)

async function loadQuestions() {
  try {
    loading.value = true
    error.value = ''
    questions.value = await getQuestions(id, 'practice')
  } catch (e: any) {
    error.value = e?.message || '加载失败'
  } finally {
    loading.value = false
  }
}

function prevQuestion() {
  if (currentIndex.value > 0) {
    currentIndex.value--
  }
}

function nextQuestion() {
  if (currentIndex.value < questions.value.length - 1) {
    currentIndex.value++
  }
}

async function onSubmit() {
  try {
    submitting.value = true
    const resp = await submitPaper(id, { durationSeconds: 0, answers: answers.value })
    result.value = resp
    showResult.value = true
  } catch (e: any) {
    error.value = e?.message || '提交失败（需要先登录）'
  } finally {
    submitting.value = false
  }
}

function goToRecords() {
  router.push('/me/records')
}

function goBack() {
  router.push(`/paper/${id}`)
}

onMounted(loadQuestions)
</script>

<template>
  <div class="practice-page">
    <LoadingState v-if="loading" text="正在加载题目..." />

    <ErrorState
      v-else-if="error && !questions.length"
      :message="error"
      @retry="loadQuestions"
    />

    <template v-else-if="questions.length">
      <!-- 进度条 -->
      <div class="progress-bar">
        <NProgress
          type="line"
          :percentage="progress"
          :show-indicator="false"
          :height="8"
          :border-radius="4"
        />
        <div class="progress-text">
          {{ currentIndex + 1 }} / {{ questions.length }}
        </div>
      </div>

      <!-- 题目卡片 -->
      <NCard class="question-card" :bordered="false">
        <div class="question-header">
          <span class="question-number">{{ currentQuestion.questionNumber }}</span>
          <span class="question-type">{{ currentQuestion.type }}</span>
        </div>

        <div class="question-content">
          {{ currentQuestion.content }}
        </div>

        <!-- 选择题选项 -->
        <div v-if="currentQuestion.options?.length" class="options-list">
          <NRadioGroup v-model:value="answers[String(currentQuestion.id)]">
            <div
              v-for="opt in currentQuestion.options"
              :key="opt.key"
              class="option-item"
              :class="{ selected: answers[String(currentQuestion.id)] === opt.key }"
            >
              <NRadio :value="opt.key">
                <span class="option-key">{{ opt.key }}</span>
                <span class="option-content">{{ opt.content }}</span>
              </NRadio>
            </div>
          </NRadioGroup>
        </div>

        <!-- 填空题输入 -->
        <div v-else class="input-answer">
          <NInput
            v-model:value="answers[String(currentQuestion.id)]"
            placeholder="请输入你的答案"
            size="large"
          />
        </div>
      </NCard>

      <!-- 导航按钮 -->
      <div class="nav-buttons">
        <NButton
          size="large"
          :disabled="currentIndex === 0"
          @click="prevQuestion"
        >
          上一题
        </NButton>

        <NButton
          v-if="currentIndex < questions.length - 1"
          type="primary"
          size="large"
          @click="nextQuestion"
        >
          下一题
        </NButton>

        <NButton
          v-else
          type="success"
          size="large"
          :loading="submitting"
          @click="onSubmit"
        >
          提交试卷
        </NButton>
      </div>

      <!-- 答题进度 -->
      <div class="answer-progress">
        已答 {{ answeredCount }} / {{ questions.length }} 题
      </div>
    </template>

    <!-- 结果弹窗 -->
    <NModal v-model:show="showResult" preset="card" title="🎉 提交成功" style="max-width: 400px">
      <div class="result-content">
        <div class="result-score">{{ result?.score }}</div>
        <div class="result-label">分</div>
      </div>
      <template #footer>
        <NSpace justify="center">
          <NButton @click="goBack">返回试卷</NButton>
          <NButton type="primary" @click="goToRecords">查看记录</NButton>
        </NSpace>
      </template>
    </NModal>
  </div>
</template>

<style scoped>
.practice-page {
  max-width: 720px;
  margin: 0 auto;
  padding: var(--spacing-6) var(--spacing-4);
}

/* Progress bar area */
.progress-bar {
  margin-bottom: var(--spacing-6);
  background: var(--color-bg-card);
  border-radius: var(--radius-xl);
  padding: var(--spacing-4) var(--spacing-5);
  box-shadow: var(--shadow-card);
}

.progress-text {
  text-align: center;
  margin-top: var(--spacing-2);
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
}

/* Question card */
.question-card {
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow-card);
  margin-bottom: var(--spacing-6);
  transition: box-shadow var(--transition-normal);
}

.question-card:hover {
  box-shadow: var(--shadow-card-hover);
}

.question-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  margin-bottom: var(--spacing-5);
}

.question-number {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-dark));
  color: #fff;
  border-radius: var(--radius-full);
  font-weight: var(--font-weight-bold);
  font-size: var(--font-size-sm);
  box-shadow: 0 2px 8px rgba(91, 141, 239, 0.3);
  flex-shrink: 0;
}

.question-type {
  background: var(--color-bg-card-hover);
  padding: var(--spacing-1) var(--spacing-3);
  border-radius: var(--radius-full);
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
  font-weight: var(--font-weight-medium);
}

.question-content {
  font-size: var(--font-size-lg);
  line-height: var(--line-height-relaxed);
  color: var(--color-text-primary);
  margin-bottom: var(--spacing-6);
}

/* Options */
.options-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-3);
}

.option-item {
  padding: var(--spacing-4) var(--spacing-5);
  border: 2px solid var(--color-border);
  border-radius: var(--radius-lg);
  transition: all var(--transition-normal);
  cursor: pointer;
  position: relative;
}

.option-item:hover {
  border-color: var(--color-primary-light);
  background: rgba(91, 141, 239, 0.04);
  transform: translateX(4px);
}

.option-item.selected {
  border-color: var(--color-primary);
  background: rgba(91, 141, 239, 0.08);
  box-shadow: 0 0 0 3px rgba(91, 141, 239, 0.12);
}

.option-item.selected .option-key {
  background: var(--color-primary);
  color: #fff;
}

.option-key {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border-radius: var(--radius-full);
  font-weight: var(--font-weight-bold);
  font-size: var(--font-size-sm);
  color: var(--color-primary);
  background: rgba(91, 141, 239, 0.1);
  margin-right: var(--spacing-3);
  transition: all var(--transition-fast);
  flex-shrink: 0;
}

.option-content {
  color: var(--color-text-primary);
  line-height: var(--line-height-normal);
}

.input-answer {
  margin-top: var(--spacing-4);
}

/* Navigation buttons */
.nav-buttons {
  display: flex;
  justify-content: space-between;
  gap: var(--spacing-4);
}

.nav-buttons :deep(.n-button) {
  min-width: 120px;
  border-radius: var(--radius-lg);
}

/* Answer progress */
.answer-progress {
  text-align: center;
  margin-top: var(--spacing-6);
  padding: var(--spacing-3) var(--spacing-4);
  background: var(--color-bg-card);
  border-radius: var(--radius-full);
  color: var(--color-text-muted);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  box-shadow: var(--shadow-card);
}

/* Result modal content */
.result-content {
  text-align: center;
  padding: var(--spacing-8) 0;
}

.result-score {
  font-size: 72px;
  font-weight: var(--font-weight-bold);
  background: linear-gradient(135deg, var(--color-success), var(--color-accent-blue));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1;
  margin-bottom: var(--spacing-2);
}

.result-label {
  font-size: var(--font-size-lg);
  color: var(--color-text-secondary);
  font-weight: var(--font-weight-medium);
}

@media (max-width: 768px) {
  .practice-page {
    padding: var(--spacing-4) var(--spacing-3);
  }

  .nav-buttons :deep(.n-button) {
    min-width: 100px;
  }
}
</style>
