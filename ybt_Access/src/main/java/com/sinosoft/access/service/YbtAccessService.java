package com.sinosoft.access.service;

import org.jdom2.Document;

import javax.servlet.http.HttpServletResponse;

public interface YbtAccessService {

    Document underWritingProcess(Document requestDoc);

    Document confirmProcess(Document requestDoc);
}
