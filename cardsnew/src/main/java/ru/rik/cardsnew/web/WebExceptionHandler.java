package ru.rik.cardsnew.web;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class WebExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(WebExceptionHandler.class);

	@ExceptionHandler(CannotCreateTransactionException.class)
	public String duplicateSpittleHandler() {
		return "errors/db-not-available";
	}

	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class,
			PersistenceException.class })
	public ModelAndView dataIntegrityError(HttpServletRequest req, Exception ex) {
		logger.error("Request: " + req.toString() + " raised " + ex, ex);
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", ex);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("errors/dupl-entity");
		return mav;
	}

	@ExceptionHandler({ Exception.class })
	public ModelAndView anyError(HttpServletRequest req, Exception ex) {
		logger.error("Request: " + req.toString() + " raised " + ex, ex);
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", ex);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("errors/dupl-entity");
		return mav;
	}
}
