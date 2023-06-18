# 스프링 MVC - 구조 이해

직접 만든 프레임워크 <-> 스프링 비교
* FrontController -> DispatcherServlet
* handleMappingMap -> HandleMapping
* MyHandleAdapter -> HandleAdapter
* ModelView -> ModelAndView
* viewResolver -> ViewResolver 
* MyView -> View

![front.png](images%2Ffront.png)
![spring.png](images%2Fspring.png)

결국 그림을 보면 우리가 만든 구조가 스프링 프레임워크의 구조임.\
우리가 만든 프레임워크에서 제일 핵심이 FrontController 였듯이 스프링에서는 DispatcherServlet이 가장 핵심임.

![diagram.png](images%2Fdiagram.png)
DispatcherServlet 역시 HttpServlet을 부모로 두고 있음. 그렇기에 서블릿으로 동작함. \
스프링 부트에서는 서버 실행 시 DispatcherServlet을 서블릿으로 등록하여 서버를 실행한다. 이때 경로는 모든경로(urlPatterns="/") 이다.

실행 순서는 간단하게 다음과 같다.
1. 서블릿이 호출되면 HttpServlet 안의 service()가 호출 됨.
2. 부모인 FrameworkServlet 안에 있는 Service()가 실행.
3. 여러 메소드 실행 후 Dispatcher.doDispatch()가 호출.

doDispatch 실행 시 핵심적인 코드는 아래와 같다.
```java
@SuppressWarnings("deprecation")
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request;
    HandlerExecutionChain mappedHandler = null;
        ...
    try {
        ModelAndView mv = null;
        ...
        try {
            ...
            // Determine handler for the current request.
            mappedHandler = getHandler(processedRequest);
            if (mappedHandler == null) {
                noHandlerFound(processedRequest, response);
                return;
            }

            // Determine handler adapter for the current request.
            HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

            ...
        
            // Actually invoke the handler.
            mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

            ...
        }
        catch (Exception ex) {
            dispatchException = ex;
        }
        catch (Throwable err) {
            // As of 4.3, we're processing Errors thrown from handler methods as well,
            // making them available for @ExceptionHandler methods and other scenarios.
            dispatchException = new ServletException("Handler dispatch failed: " + err, err);
        }
        processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    }
    ...
}


private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                   @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
                                   @Nullable Exception exception) throws Exception {

    ...

    // Did the handler return a view to render?
    if (mv != null && !mv.wasCleared()) {
        //렌더링 부분
        render(mv, request, response);
        ...
    }
    else {
        ...
    }

    ...
}

protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
    ...
    View view;
    String viewName = mv.getViewName();
    if (viewName != null) {
        // We need to resolve the view name.
        view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
        ...
    }
    else {
        ...
    }

    ...
    try {
        ...
        view.render(mv.getModelInternal(), request, response);
    }
    catch (Exception ex) {
        ...
    }
}



```

그 이후의 동작 순서는 우리가 만들었던 프론트 컨트롤러의 순서와 동일하다.\
서블릿에 대한 동작 원리를 한번 더 정리하고 스프링의 DispatcherServlet의 동작방식을 확실하게 알고 있으면 나중에 문제, 혹은 다른 기능 개발 시 좀 더 원할하게\
개발이 가능할 것으로 보인다.