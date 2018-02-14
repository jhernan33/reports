
        def reportName = "/home/zippyttech/Documentos/ArenListOperJ"
        def uno = reportName + '.jrxml'
        /*
            Valores del Formato de Salida
         */
        def PDF_FORMAT = 1
        def HTLM_FORMAT = 2
        def TEXT_FORMAT = 3
        def CSV_FORMAT = 4
        def XLS_FORMAT = 5
        def RTF_FORMAT = 6
        def XML_FORMAT = 7
        def DOC_FORMAT = 8
        def PPT_FORMAT = 9
        // Cargado de Variables
        def form_sali = 9 // Establecer el formato de salida del Reporte
        def paraOrde = 'titlecompania'
        try {
            String inputFile ='/home/zippyttech/Documentos/reportes/arenera/json_oper.json'
            //String fileContents = new File(inputFile).getText('UTF-8')
            /*
                String reportContents = "{}" //your json
                InputStream is = new ByteArrayInputStream(reportContent.getBytes());
                Map params = new HashMap();
                params.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params);
             */
            //InputStream fileContents = new ByteArrayInputStream(inputFile.getBytes())
            //def cade_json = new JsonSlurper() // Definition Type Json
            //def jsonObject = cade_json.parseText(fileContents)
            //File jsonfile = new File("/home/zippyttech/Documentos/reportes/arenera/json-operation.json")
            //JRDataSource dataSource = null
             //   datasource = new JsonDataSource(jsonfile)
            //JsonDataSource datasource = new JsonDataSource(new ByteArrayInputStream(jsonfile.getBytes("UTF-8")));
            // Report parameter
            Map<String, String> reportParam = new HashMap<String, String>()
            // Report parameter
            reportParam.put('p_titulo', "INFORME LISTADO DE OPERACIONES ARREGLOS...")
            reportParam.put('p_usuario', "LUIS")
            reportParam.put('p_logo', "/home/zippyttech/Documentos/reportes/logobarcelona.png")
            //reportParam.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM,fileContents)
            reportParam.put(JsonQueryExecuterFactory.JSON_SOURCE,inputFile)
            //JsonDataSource datasource = new JsonDataSource(new ByteArrayInputStream(jsonObjec.getBytes("UTF-8")))
           // JsonDataSource datasource = new JsonDataSource(jsonObject)
            //def JRDataSource dataSource = null
            //datasource = new JsonDataSource(cade_json)
            //datasource = new JsonDataSource(jsonObject)
            JasperDesign jd = JRXmlLoader.load(uno)
            //compiles jrxml
            JasperReport jr = JasperCompileManager.compileReport(jd)
            JasperPrint print = JasperFillManager.fillReport(jr, reportParam)
            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream()
            // Format out
            def JRExporter exporter
                switch (form_sali){
                    case PDF_FORMAT:
                        exporter = new JRPdfExporter()
                        break
                    case HTLM_FORMAT:
                        exporter = new JRXhtmlExporter()
                        exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,"/home/zippyttech/Documentos/reportes/images/")
                        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE)
                        exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, true)
                        break
                    case TEXT_FORMAT:
                        exporter = new JRTextExporter()
                        exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 300);
                        exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 10f);
                        exporter.setParameter(JRTextExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
                        break
                    case CSV_FORMAT:
                        exporter = new JRCsvExporter()
                        break
                    case XLS_FORMAT:
                        exporter = new JRXlsExporter()
                        break
                    case RTF_FORMAT:
                        exporter = new JRRtfExporter()
                        break
                    case XML_FORMAT:
                        exporter = new JRXmlExporter()
                        break
                    case DOC_FORMAT:
                        exporter = new JRDocxExporter()
                        break
                    case PPT_FORMAT:
                        exporter = new JRPptxExporter()
                        break
                }

            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, pdfStream) //your output goes here
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print)
          exporter.exportReport()
        } catch (Exception e) {
            render('Error al Generar el Reporte, Contacte con el Equipo de BackEnd')
            throw new RuntimeException("No es posible generar el Reporte en el Formato solicitado.", e)
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            ex.printStackTrace()
        } finally {
            switch (form_sali)
                    {
                        case PDF_FORMAT:  // Out Pdf
                            render(file: pdfStream.toByteArray(), contentType: 'application/pdf')
                            break
                        case HTLM_FORMAT:  // Out HTML
                            response.setHeader("Content-disposition", "attachment; filename=listoper.html")
                            response.setContentType("text/html")
                            response.characterEncoding = "UTF-8"
                            response.outputStream << pdfStream.toByteArray()
                        break
                        case TEXT_FORMAT: // Out Text
                            response.setHeader("Content-disposition", "attachment; filename=listoper.txt")
                            response.setContentType("text/plain")
                            response.characterEncoding = "ISO-8859-1"
                            response.outputStream << pdfStream.toByteArray()
                            break
                        case CSV_FORMAT: // Out CSV
                            response.setHeader("Content-disposition", "attachment; filename=listoper"+ "." + "csv")
                            response.contentType = "application/CSV"
                            response.characterEncoding = "UTF-8"
                            response.outputStream << pdfStream.toByteArray()
                        break
                        case XLS_FORMAT: // Out XLS
                            response.setHeader("Content-disposition", "attachment; filename=listoper"+ "." + "xls")
                            response.contentType = "application/vnd.ms-excel"
                            response.characterEncoding = "UTF-8"
                            response.outputStream << pdfStream.toByteArray()
                        break
                        case RTF_FORMAT: // Out Rtf
                            response.setHeader("Content-disposition", "attachment; filename=listoper.rtf")
                            response.setContentType("text/rtf")
                            response.characterEncoding = "UTF-8"
                            response.outputStream << pdfStream.toByteArray()
                            break
                        case XML_FORMAT: // Out Xml
                            response.setHeader("Content-disposition", "attachment; filename=listoper.xml")
                            response.setContentType("text/xml")
                            response.characterEncoding = "UTF-8"
                            response.outputStream << pdfStream.toByteArray()
                            break
                        case DOC_FORMAT: // Out DOCX
                        response.setHeader("Content-disposition", "attachment; filename=listoper.docx");
                        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                        response.characterEncoding = "UTF-8"
                        response.outputStream << pdfStream.toByteArray()
                        break
                        case PPT_FORMAT: // Out Ppt
                            response.setHeader("Content-disposition", "attachment; filename=listoper.pptx");
                            response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation")
                            response.characterEncoding = "UFT-8"
                            response.outputStream << pdfStream.toByteArray()
                            break
                    }
        }
