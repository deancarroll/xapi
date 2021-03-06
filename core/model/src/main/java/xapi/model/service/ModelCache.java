package xapi.model.service;

import xapi.model.api.Model;
import xapi.util.api.SuccessHandler;

public interface ModelCache {

  Model getModel(String key);
  void cacheModel(Model model, SuccessHandler<Model> callback);
  void saveModel(Model model, SuccessHandler<Model> callback);
  void deleteModel(Model model, SuccessHandler<Model> callback);
  
}
