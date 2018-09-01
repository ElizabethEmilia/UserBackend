<template>
     <Card  class="card-margin">
         <Divider orientation="left"><h3>温馨提示</h3></Divider>
        
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
