<template>
    <div>
        <p v-for="(e, i) in Array(10).fill(0)" :key="i">
            {{ e }}<br/>
        </p>
    </div>
</template>

<script>
export default {
    props: ['content'],
}
</script>
