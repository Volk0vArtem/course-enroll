# Course Enrol

Course Enroll — это приложение для управления курсами и их регистрацией студентов. Оно позволяет добавлять, редактировать, просматривать и удалять курсы, управлять студентами и их регистрацией на доступные курсы.

## Функционал

### Для курсов:
- Добавление нового курса с указанием лимита студентов, времени начала и окончания.
- Просмотр всех курсов, в том числе фильтрация доступных курсов.
- Удаление курса.
- Редактирование курса.

### Для студентов:
- Регистрация нового студента.
- Получение информации о конкретном студенте.
- Просмотр списка всех студентов.
- Удаление студента.
- Редактирование информации о студенте.

### Для регистрации студентов на курсы:
- Регистрация студента на доступный курс.
- Удаление регистрации студента с курса.
- Проверка доступности курса для регистрации (ограничение по времени и количеству студентов).

## Стек технологий

- **Java 21**
- **Spring Boot 3**
- **PostgreSQL**
- **H2**
- **JUnit 5**
- **Lombok**

## API

### Курсы
- `POST /courses` — добавить новый курс.
  - Тело запроса: `CourseDto`
  - Ответ: созданный курс (`201 Created`)
- `GET /courses` — получить все курсы.
  - Ответ: список курсов (`200 OK`)
- `GET /courses/available` — получить доступные курсы.
  - Ответ: список доступных курсов (`200 OK`)
- `GET /courses/{courseId}` — получить курс по ID.
  - Параметры: `courseId` (ID курса)
  - Ответ: информация о курсе (`200 OK`)
- `PATCH /courses/{courseId}` — обновить информацию о курсе.
  - Параметры: `courseId` (ID курса)
  - Тело запроса: обновляемые поля курса
  - Ответ: обновленный курс (`200 OK`)
- `DELETE /courses/{courseId}` — удалить курс.
  - Параметры: `courseId` (ID курса)
  - Ответ: без тела (`204 No Content`)

### Студенты
- `POST /students` — зарегистрировать нового студента.
  - Тело запроса: `StudentDto`
  - Ответ: созданный студент (`201 Created`)
- `GET /students` — получить всех студентов.
  - Ответ: список студентов (`200 OK`)
- `GET /students/{studentId}` — получить студента по ID.
  - Параметры: `studentId` (ID студента)
  - Ответ: информация о студенте (`200 OK`)
- `PATCH /students/{studentId}` — обновить информацию о студенте.
  - Параметры: `studentId` (ID студента)
  - Тело запроса: обновляемые поля студента
  - Ответ: обновленный студент (`200 OK`)
- `DELETE /students/{studentId}` — удалить студента.
  - Параметры: `studentId` (ID студента)
  - Ответ: без тела (`204 No Content`)

### Регистрация на курс
- `POST /enrollments/{courseId}/{studentId}` — зарегистрировать студента на курс.
  - Параметры: `courseId` (ID курса), `studentId` (ID студента)
  - Ответ: созданная регистрация (`201 Created`)
- `DELETE /enrollments/{courseId}/{studentId}` — удалить регистрацию студента с курса.
  - Параметры: `courseId` (ID курса), `studentId` (ID студента)
  - Ответ: без тела (`204 No Content`)
