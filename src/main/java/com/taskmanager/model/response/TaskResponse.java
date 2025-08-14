    package com.taskmanager.model.response;

    import com.taskmanager.entity.Task;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class TaskResponse {
        private Long id;
        private String name;
        private String description;

        public static TaskResponse from(Task task) {
            return TaskResponse.builder()
                    .id(task.getId())
                    .name(task.getName())
                    .description(task.getDescription())
                    .build();
        }
    }
