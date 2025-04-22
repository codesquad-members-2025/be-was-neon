package webserver.view;

import webserver.model.Model;

public class ModelAndView {

    private final String viewName;
    private final Model model;

    public ModelAndView(String viewName) {
        this(viewName, new Model());
    }

    public ModelAndView(String viewName, Model model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Model getModel() {
        return model;
    }

}
