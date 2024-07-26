# FarmPro-Backend

## 프로젝트 소개

> FarmPro는 생성형 AI를 사용하여 농업 생산자에게 최적의 마케팅 전략을 제공합니다.  
> 이 레포지토리는 FarmPro의 Backend infra, ML Service를 담당합니다.

![FarmPro-logo.png](docs/FarmPro-logo.png)

## API 문서

[API Documentation](https://port-0-farmpro-backend-lypd5head859a5e8.sel5.cloudtype.app/swagger-ui/index.html)

## 기술 스택

- `Java` 17
- `Spring Boot` 3.3.1
    - `Spring Data JPA`
    - `Spring WebFlux`
    - `Spring Cloud` 2020.0.3
    - `Spring Cloud AWS` 2.4.0

## 사용한 외부 API
- OpenAI DALL-E (Image generation)
- OpenA chatGPT (text)

## 주요 기능

| ![image](https://github.com/user-attachments/assets/8b64544e-93fc-41c1-bbb7-77e195fe9ca1) | ![image](https://github.com/user-attachments/assets/d9be6803-5ca6-478b-a06c-4f9419798e97) | ![image](https://github.com/user-attachments/assets/92eb5753-48b8-46ea-85af-170f6cc44612) |
|:-----------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------:|
| ![image](https://github.com/user-attachments/assets/0d662ddc-f395-4514-b9b5-e81dd83904aa) | ![image](https://github.com/user-attachments/assets/5a1b9b62-213f-4efc-b886-3b0a52e2965d) | ![image](https://github.com/user-attachments/assets/4a1fb099-e1cf-43dc-bf80-1155285ef6ba) |

## Development

1. Install depedencies

```shell
./gradlew build
```

2. Execute project

```shell
java -jar farmpro-1.0.0-SNAPSHOT.jar
```

- `farmpro-1.0.0-SNAPSHOT.jar` 파일은 `build/libs` 디렉토리에 생성됩니다.
- [Release 1.0.0](https://github.com/Art-Chain/FarmPro-Backend/archive/refs/tags/1.0.0.zip) 파일을 직접 다운로드 받아 실행할 수 있습니다.

## 프로젝트 구조

```Bash

├── LICENSE
├── README.md
├── build
├── build.gradle
├── docs
├── gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── java
│   └── artchain
│       └── farmpro
│           ├── FarmproApplication.java
│           ├── ai
│           │   ├── ChatGptConfiguration.java
│           │   ├── ChatGptWebClient.java
│           │   ├── dto
│           │   │   ├── ChatGptResponse.java
│           │   │   └── GPTRecommendResponse.java
│           │   └── prompt
│           │       ├── PromptContext.java
│           │       ├── PromptPropertyParser.java
│           │       ├── PromptStrategy.java
│           │       ├── SnsContentPromptStrategy.java
│           │       └── SnsImagePromptStrategy.java
│           ├── card
│           │   ├── Card.java
│           │   ├── CardRepository.java
│           │   ├── CardStyle.java
│           │   └── dto
│           │       ├── CardRequest.java
│           │       └── CardsRequest.java
│           ├── content
│           │   ├── Content.java
│           │   ├── ContentController.java
│           │   ├── ContentPurpose.java
│           │   ├── ContentRepository.java
│           │   ├── ContentService.java
│           │   ├── ContentType.java
│           │   ├── ParlanceStyle.java
│           │   ├── dto
│           │   │   ├── ContentCreateResponse.java
│           │   │   ├── ContentRecommendRequest.java
│           │   │   ├── ContentRecommendResponse.java
│           │   │   ├── ContentRequest.java
│           │   │   ├── ContentResponse.java
│           │   │   ├── ContentResponses.java
│           │   │   ├── ProjectInfoRequest.java
│           │   │   └── ProjectInfoResponse.java
│           │   └── image
│           │       ├── ContentImage.java
│           │       ├── ContentImageController.java
│           │       ├── ContentImageService.java
│           │       └── dto
│           │           ├── ContentImagePresignedUrlVO.java
│           │           ├── ContentImageResponse.java
│           │           ├── ContentImageResponses.java
│           │           └── NotifyContentImageSaveSuccessRequest.java
│           ├── crop
│           │   ├── Crop.java
│           │   ├── CropController.java
│           │   ├── CropRepository.java
│           │   ├── CropService.java
│           │   └── dto
│           │       ├── CropRequest.java
│           │       ├── CropResponse.java
│           │       └── CropResponses.java
│           ├── global
│           │   ├── config
│           │   │   ├── S3Config.java
│           │   │   ├── SwaggerConfig.java
│           │   │   └── WebConfig.java
│           │   └── health
│           │       └── HealthController.java
│           └── selectedcrop
│               ├── SelectedCrop.java
│               └── SelectedCropRepository.java

