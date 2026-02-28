<script setup lang="ts">
import { ref, h, onMounted, reactive } from 'vue'
import {
  NDataTable, NButton, NSpace, NInput, NSelect, NTag, NUpload, NCard,
  NPopconfirm, useMessage, type DataTableColumns, type UploadFileInfo,
} from 'naive-ui'
import type { Paper, PageResult } from '@/types/models'
import {
  getAdminPapers, importPaperJson, publishPaper, unpublishPaper, deletePaper,
} from '@/services/admin'

const message = useMessage()
const loading = ref(false)
const data = ref<Paper[]>([])
const pagination = reactive({
  page: 1,
  pageSize: 10,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50],
})
const filters = reactive({
  status: null as string | null,
  title: '',
})

const statusOptions = [
  { label: '全部', value: '' },
  { label: '草稿', value: 'DRAFT' },
  { label: '已发布', value: 'PUBLISHED' },
]

async function loadData() {
  loading.value = true
  try {
    const res = await getAdminPapers({
      current: pagination.page,
      size: pagination.pageSize,
      status: filters.status || undefined,
      title: filters.title || undefined,
    })
    data.value = res.records
    pagination.itemCount = res.total
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function handlePageChange(page: number) {
  pagination.page = page
  loadData()
}

function handlePageSizeChange(pageSize: number) {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadData()
}

function handleSearch() {
  pagination.page = 1
  loadData()
}

async function handlePublish(row: Paper) {
  try {
    await publishPaper(row.id)
    message.success('发布成功')
    loadData()
  } catch (e: any) {
    message.error(e.message || '操作失败')
  }
}

async function handleUnpublish(row: Paper) {
  try {
    await unpublishPaper(row.id)
    message.success('已取消发布')
    loadData()
  } catch (e: any) {
    message.error(e.message || '操作失败')
  }
}

async function handleDelete(row: Paper) {
  try {
    await deletePaper(row.id)
    message.success('删除成功')
    loadData()
  } catch (e: any) {
    message.error(e.message || '操作失败')
  }
}

async function handleUpload({ file }: { file: UploadFileInfo }) {
  if (!file.file) return
  try {
    await importPaperJson(file.file)
    message.success('导入成功')
    loadData()
  } catch (e: any) {
    message.error(e.message || '导入失败')
  }
}

const columns: DataTableColumns<Paper> = [
  { title: 'ID', key: 'id', width: 60 },
  { title: '标题', key: 'title', ellipsis: { tooltip: true } },
  { title: '年份', key: 'year', width: 80 },
  { title: '月份', key: 'month', width: 80 },
  { title: '类型', key: 'type', width: 80 },
  { title: '难度', key: 'difficulty', width: 80 },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render(row) {
      return h(
        NTag,
        { type: row.status === 'PUBLISHED' ? 'success' : 'warning', size: 'small' },
        { default: () => (row.status === 'PUBLISHED' ? '已发布' : '草稿') },
      )
    },
  },
  { title: '练习次数', key: 'attempts', width: 90 },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    render(row) {
      return h(NSpace, { size: 'small' }, () => [
        row.status === 'DRAFT'
          ? h(NButton, { size: 'small', type: 'primary', onClick: () => handlePublish(row) }, { default: () => '发布' })
          : h(NButton, { size: 'small', onClick: () => handleUnpublish(row) }, { default: () => '取消发布' }),
        h(
          NPopconfirm,
          { onPositiveClick: () => handleDelete(row) },
          {
            trigger: () => h(NButton, { size: 'small', type: 'error' }, { default: () => '删除' }),
            default: () => '确认删除该试卷？',
          },
        ),
      ])
    },
  },
]

onMounted(loadData)
</script>

<template>
  <div class="papers-page">
    <h2 class="page-title">试卷管理</h2>
    <NCard>
      <div class="toolbar">
        <NSpace>
          <NInput
            v-model:value="filters.title"
            placeholder="搜索标题"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
          <NSelect
            v-model:value="filters.status"
            :options="statusOptions"
            placeholder="状态筛选"
            clearable
            style="width: 120px"
            @update:value="handleSearch"
          />
          <NButton type="primary" @click="handleSearch">搜索</NButton>
        </NSpace>
        <NUpload
          :show-file-list="false"
          accept=".json"
          :custom-request="({ file }: any) => handleUpload({ file })"
        >
          <NButton type="info">导入JSON</NButton>
        </NUpload>
      </div>
      <NDataTable
        :columns="columns"
        :data="data"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row: Paper) => row.id"
        remote
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
      />
    </NCard>
  </div>
</template>

<style scoped>
.papers-page {
  max-width: 1200px;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--color-text-primary, #1a1a1a);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  flex-wrap: wrap;
  gap: 12px;
}
</style>
