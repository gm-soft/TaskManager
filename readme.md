# Project Manager (Менеджер проектов)

## Описание проекта
Прежнее описание проекта доступно [здесь](https://github.com/maximgorbatyuk/TaskManager/blob/master/olddescription.md). Менеджер проектов - приложение для подсчета эффективности работы пользователя в денежном эквиваленте(и во временном тоже). Приложение призвано помочь пользователю - работнику абсолютно любой сферы деятельности - подсчитать полезное время работы из рабочего дня и помочь повысить эффективность. Юзкейс: пользователь, когда приступает непосредственно к той деятельности, с которой связана его профессия, запускает "секудомер" приложения, а при прекращении - останавливает. Разница времени записывается в статистику приложения. Под полезной работой подразумевается та деятельность, направленная на достижение результата, исключающая различные перерывы, совещания и тд. По завершению определенного этапа работы (зарплата, закрытие проекта, отчетности) пользователь изменяет статус проекта на "завершенный", вносит стоиомсть проекта (полученный результат в денежном эквиваленте), и программа подсчитывает статистические данные, эффективность работы и стоимость часа полезной работы (и неполезной тоже). На данный момент (13.04.2016) реализовано около половины всех планируемых функций.

## Планы (начиная от 13.04.2016)
* Создание настроек, где пользовательн вносит свой рабочий календарь (кол-во часов рабочего дня в сутки), возможно, род своей деятельности. Пункты меню еще не продуманы до конца
* Создание фонового процесса таймера, который бы создавался по уничтожению активити и выгрузке приложения из памяти системой (или юзером). Открытие активити таймера по возобновлению приложения.
* Создание страницы сводки статистики. Создание заполнения статистики в базу данных, естественно. (Или нет, просто анализировать по открытию активити, хз пока).



## Планируемые фичи (до 13.04.2016)
* (не нужно) Сокрытие задачи по изменению ее статуса
* [x]Отображение всех задач и невыполненных
* [x]CRUD - операции (Create, Read, Update, Destroy)
* (не нужно) Уведомления приближающихся задач
* Создание фонового процесса отслеживания времени

## Долгосрочные планы
Применить приложение для работы с Веб-запросами и синхронизацией. Организовать работу сервера на базе Гугл-скриптов и веб-приложения, которое отвечало бы на Get и Post запросы. За основу базы данных берется гугл-таблица. Скорее всего, работа будет Супер-медленной, однако это не проверено пока что. Но идея такая есть. 

## Версии и журнал
* 14/04/2016. Проделал работу по рефакторингу UI. Внедрил и проэкспериментировал с сервисами в Андроид. Переместил в него таймер и настроил Broadcast на ежесекундную отправку строки с миллисекундами разницы по системе. В активити настроил BroadcastReciever, который сканировал по Тэгу события и отображал передаваемое мною. 

Была проблема. Я сумел остановить процесс, однако в системе осталась строка по фильтру, и чем больше раз я запускал таймер, тем больше строк подгружались в активити из системы. Это значит, что вместо актуального времени в текствью записывалась и нулевая строка. ВОпрос пока не решал и не искал на стаке. Переместил назад пока что счетчик в активити, закомментировал процедуры с сервисами. 

Также выполнил юзкейс, когда при закрытии активити со счетчиком данные по проекту (время исполнения) обновлялись. Дополнил Активити редактирования заказа, тееперь там вводится стоимость проекта.


## Ресурсы
[Ссылка](https://github.com/maximgorbatyuk/TaskManager/blob/master/source.md) на список используемых (и неиспользуемых) ресурсов