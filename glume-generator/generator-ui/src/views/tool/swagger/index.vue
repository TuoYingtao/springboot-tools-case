<template>
   <i-frame v-model:src="url"></i-frame>
</template>

<script setup>
import iFrame from '@/components/Framework/iFrame/index.vue'

const url = ref(import.meta.env.VITE_APP_BASE_API + "/doc.html")
</script>
