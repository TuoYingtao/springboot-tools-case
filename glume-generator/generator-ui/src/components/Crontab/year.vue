<template>
  <el-form size="small">
    <el-form-item>
      <el-radio :label="1" v-model='radioValue'>
        不填，允许的通配符[, - * /]
      </el-radio>
    </el-form-item>

    <el-form-item>
      <el-radio :label="2" v-model='radioValue'>
        每年
      </el-radio>
    </el-form-item>

    <el-form-item>
      <el-radio :label="3" v-model='radioValue'>
        周期从
        <el-input-number v-model='cycle01' :min='cycleRange01.min' :max="cycleRange01.max"/>
        -
        <el-input-number v-model='cycle02' :min="cycleRange02.min" :max="cycleRange02.max"/>
      </el-radio>
    </el-form-item>

    <el-form-item>
      <el-radio :label="4" v-model='radioValue'>
        从
        <el-input-number v-model='average01' :min='averageRange01.min' :max="averageRange01.max"/>
        年开始，每
        <el-input-number v-model='average02' :min="1" :max="10"/>
        年执行一次
      </el-radio>

    </el-form-item>

    <el-form-item>
      <el-radio :label="5" v-model='radioValue'>
        指定
        <el-select clearable v-model="checkboxList" placeholder="可多选" multiple :multiple-limit="8">
          <el-option v-for="(item, index) in checkRangeFullYear" :key="index" :value="item.value" :label="item.label"/>
        </el-select>
      </el-radio>
    </el-form-item>
  </el-form>
</template>

<script setup name="Year" lang="ts">
const emit = defineEmits(['update'])
const props = defineProps({
  cron: {
    type: Object,
    default: {
      second: "*",
      min: "*",
      hour: "*",
      day: "*",
      month: "*",
      week: "?",
      year: ""
    }
  },
  check: {
    type: Function,
    default: () => {
    }
  }
})
const minFactor = 9
const maxFactor = 10
const currentFullYear: number = Number(new Date().getFullYear())

const radioValue = ref<number>(1)
const cycle01 = ref<number>(currentFullYear)
const cycle02 = ref<number>(currentFullYear + 1)
const cycleRange01 = reactive<Record<string, number>>({
  min: currentFullYear,
  max: currentFullYear + minFactor,
});
const cycleRange02 = reactive<Record<string, number>>({
  min: currentFullYear + 1,
  max: currentFullYear + maxFactor,
});
const average01 = ref<number>(currentFullYear)
const average02 = ref<number>(1)
const averageRange01 = reactive<Record<string, number>>({
  min: currentFullYear,
  max: currentFullYear + minFactor,
})
const checkboxList = ref<number[]>([])
const checkCopy = ref<number[]>([currentFullYear])
const checkRangeFullYear = computed(() => {
  const array = [];
  for (let i = 0; i < maxFactor + 1; i++) {
    const year = currentFullYear + i
    array.push({ label: year, value: year });
  }
  return array;
})

const cycleTotal = computed(() => {
  cycle01.value = props.check(cycle01.value, cycleRange01.min, cycleRange01.max)
  cycle02.value = props.check(cycle02.value, cycleRange02.min, cycleRange02.max)
  return cycle01.value + '-' + cycle02.value
})
const averageTotal = computed(() => {
  average01.value = props.check(average01.value, averageRange01.min, averageRange01.max)
  average02.value = props.check(average02.value, 1, 10)
  return average01.value + '/' + average02.value
})
const checkboxString = computed(() => {
  return checkboxList.value.join(',')
})

watch(() => cycle01.value, newVal => cycleRange02.min = newVal + 1);
watch(() => props.cron.year, value => changeRadioValue(value))
watch([radioValue, cycleTotal, averageTotal, checkboxString], () => onRadioChange())

function changeRadioValue(value: string) {
  if (value === '') {
    radioValue.value = 1
  } else if (value === "*") {
    radioValue.value = 2
  } else if (value.indexOf("-") > -1) {
    const indexArr = value.split('-')
    cycle01.value = Number(indexArr[0])
    cycle02.value = Number(indexArr[1])
    radioValue.value = 3
  } else if (value.indexOf("/") > -1) {
    const indexArr = value.split('/')
    average01.value = Number(indexArr[0])
    average02.value = Number(indexArr[1])
    radioValue.value = 4
  } else {
    checkboxList.value = [...new Set(value.split(',').map(item => Number(item)))]
    radioValue.value = 5
  }
}

function onRadioChange() {
  switch (radioValue.value) {
    case 1:
      emit('update', 'year', '', 'year')
      break
    case 2:
      emit('update', 'year', '*', 'year')
      break
    case 3:
      emit('update', 'year', cycleTotal.value, 'year')
      break
    case 4:
      emit('update', 'year', averageTotal.value, 'year')
      break
    case 5:
      // if (checkboxList.value.length === 0) {
      //   checkboxList.value.push(...checkCopy.value)
      // } else {
      //   checkCopy.value = checkboxList.value
      // }
      emit('update', 'year', checkboxString.value, 'year')
      break
  }
}
</script>

<style lang="scss" scoped>
.el-input-number--small, .el-select, .el-select--small {
  margin: 0 0.2rem;
}

.el-select, .el-select--small {
  width: 18.8rem;
}
</style>
