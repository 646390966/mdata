import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const settingStore = defineStore('setting', () => {
    //state
    const flag = ref(true);
    //getter
    const flagReverse = computed(() => !flag.value);
    //actions
    function reverse() {
        
        flag.value=!flag.value
    }
  
    return { flag, flagReverse, reverse }
  })