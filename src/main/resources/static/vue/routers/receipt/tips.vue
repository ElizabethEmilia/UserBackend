<template>
     <Card title="温馨提示"  class="card-margin">
        {{ tipsContent }}
    </Card>
</template>

<script>
export default {
    data: () => ({
        tipsContent: '没有'
    }) 
}
</script>
