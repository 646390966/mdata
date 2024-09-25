<template>
  <img v-if="imageUrl" :src="newImageUrl" :width="width" :height="height"
    style="cursor:pointer;object-fit: cover;" @click="clickPicture" />
  <div class="modal-container" v-show="show">
    <div class="modal-content">
      <el-icon size="large" style="cursor:pointer;position:absolute;top:0;right:0" @click="show = false">
        <CircleClose></CircleClose>
      </el-icon>
      <img :src="newImageUrl" width="500" height="500" style="object-fit: cover;" />
    </div>
  </div>
</template>

<script setup lang="ts" name="Picture">
import { isCancel } from 'axios';
import { ref,computed } from 'vue'
const show = ref(false)
const props = defineProps({

  imageUrl: {
    type: String,
    default: ''
  },
  width: {
    type: Number,
    default: 100
  },
  height: {
    type: Number,
    default: 100
  },
  isClick: {
    type: Boolean,
    default: true
  }
})

const clickPicture = () => {

  if (props.isClick) {
    show.value = true
  }
}
const newImageUrl = computed(() => {

  if (props.imageUrl&&!props.imageUrl.includes("data:image")) {
    return "data:image/png;base64," + props.imageUrl
  } else {
    return props.imageUrl
  }

});
</script>

<style scoped>
.modal-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 9999;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;

}

.modal-content {
  background-color: #fff;
  position: relative;

}
</style>