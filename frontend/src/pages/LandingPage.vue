<script setup lang="ts">
import LandingHeader from '@/components/landing/LandingHeader.vue'
import HeroSection from '@/components/landing/HeroSection.vue'
import FeatureCards from '@/components/landing/FeatureCards.vue'
import ProductShowcase from '@/components/landing/ProductShowcase.vue'
import SocialProof from '@/components/landing/SocialProof.vue'
import CTASection from '@/components/landing/CTASection.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'
</script>

<template>
  <div class="landing-page">
    <LandingHeader />
    <main>
      <HeroSection />
      <FeatureCards />
      <ProductShowcase />
      <SocialProof />
      <CTASection />
    </main>
    <LandingFooter />
  </div>
</template>

<style scoped>
.landing-page {
  scroll-behavior: smooth;
}

main {
  overflow: hidden;
}
</style>
