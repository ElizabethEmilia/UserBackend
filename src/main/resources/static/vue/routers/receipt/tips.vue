<template>
     <Card title="温馨提示"  class="card-margin">
        {{ receipttips }}
    </Card>
</template>

<script>
    import { receipttips } from '../../../data/tips.js';

export default {
    data: () => ({
        receipttips
    }) 
}
</script>
