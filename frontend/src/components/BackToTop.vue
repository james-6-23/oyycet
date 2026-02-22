<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { NButton, NIcon } from 'naive-ui'
import { ArrowUpOutline } from '@vicons/ionicons5'

const visible = ref(false)

function handleScroll() {
  visible.value = window.scrollY > 300
}

function scrollToTop() {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<template>
  <Transition name="fade-scale">
    <NButton
      v-if="visible"
      class="back-to-top"
      type="primary"
      circle
      size="large"
      @click="scrollToTop"
    >
      <template #icon>
        <NIcon :component="ArrowUpOutline" />
      </template>
    </NButton>
  </Transition>
</template>

<style scoped>
.back-to-top {
  position: fixed;
  right: 24px;
  bottom: 24px;
  z-index: 99;
  box-shadow: var(--shadow-card-hover);
}

.fade-scale-enter-active,
.fade-scale-leave-active {
  transition: all 0.2s ease;
}

.fade-scale-enter-from,
.fade-scale-leave-to {
  opacity: 0;
  transform: scale(0.8);
}

@media (max-width: 768px) {
  .back-to-top {
    right: 16px;
    bottom: 16px;
  }
}
</style>
