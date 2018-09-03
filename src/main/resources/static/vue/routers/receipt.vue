<template>
    <div>
        <Tips />
        <Application />
        <Stat />
    </div>
</template>

<script>
import Tips from './receipt/tips.vue';
import Application from './receipt/application.vue';
import Stat from './receipt/stat.vue';

export default {
    components: {
        Tips, Application, Stat
    },
    data: () => ({

    })
}
</script>