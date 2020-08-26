<template>
  <transition mode="out-in"
              enter-active-class="animate__animated animate__bounceInDown animate__faster"
              leave-active-class="animate__animated animate__bounceOutDown animate__faster">
    <router-view/>
  </transition>


</template>

<script>
    export default {
        name: "myMain"
    }
</script>

<style>
</style>
