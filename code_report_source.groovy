def report_jasper() {
        def Connection conn = null;
        //def reportName = "/home/zippyttech/Documentos/test"
        def reportName = "/home/zippyttech/Documentos/ArenListOper"
        //def datasour ='/home/zippyttech/Documentos/DataAdapter.xml'
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
        def form_sali = 7 // Establecer el formato de salida del Reporte
        def user = "postgres"
        def password = "1234"
        def stringQuery = null
        def paraOrde = 'titlecompania'
        try {
            Class.forName("org.postgresql.Driver");
            //def con = DriverManager.getConnection("jdbc:postgresql://172.17.0.2:5432/store_dev", user, password);
            def con = DriverManager.getConnection("jdbc:postgresql://172.17.0.2:5432/arenera_dev", user, password);
            // Report parameter
            Map<String, String> reportParam = new HashMap<String, String>()
            // Report parameter
            //reportParam.put('ptitu', "INFORME DE PRODUCTOS")
            reportParam.put('p_titulo', "INFORME LISTADO DE OPERACIONES ARREGLOS...")
            reportParam.put('p_usuario', "LUIS")
            reportParam.put('p_logo', "/home/zippyttech/Documentos/reportes/logobarcelona.png")
            //reportParam.put('p_orde_basi', "reference_date")
            reportParam.put('p_orde_basi', paraOrde)
            stringQuery ="select this_.operation_id as operation_id,this_.title as title,this_.detail as detail,this_.icon as icon,this_.enabled as enable,this_.deleted as deleted,this_.visible as visible,this_.editable as editable,account_al1_.account_id as account_id,account_al1_.name as name,account_al1_.ruc as ruc,account_al1_.email as email,this_.volume as volume,this_.price as price,this_.payment as payment,this_.invoice as invoice,this_.images as images,recharges9_.quantity as quantity,recharges9_.code as code,recharges9_.reference_date as reference_date,recharges9_.paid as paid,recharges9_.recharge_type_code as recharge_type_code,recharges9_.recharge_type_id as recharge_type_id,recharges9_.recharge_type_title as recharge_type_title,recharges9_.recharge_id as recharge_id,company3_.company_id as company_id,company3_.code as code,company3_.title as titlecompania,container4_.container_id as container_id,container4_.title as container_title,driver5_.driver_id as driver_id,driver5_.code as code,driver5_.title as title,place6_.place_id as place_id,place6_.code as code,place6_.title as title,sandtype7_.sand_type_id as sand_type_id,sandtype7_.title as title,vehicle2_.vehicle_id as vehicle_id,vehicle2_.code as vehicle_code,vehicle2_.title as vehicle_title,this_.username_creator as username_creator,this_.username_updater as username_updater,this_.date_created as date_created,this_.date_updated as date_updated,this_.user_agent as user_agent,this_.ip as ip ,company_type5.title as company_type from operation this_ inner join account account_al1_ on this_.account_id=account_al1_.account_id inner join company company3_ on this_.company_id=company3_.company_id left outer join company_type company_type5 on company3_.company_type_id = company_type5.company_type_id left outer join container container4_ on this_.container_id=container4_.container_id left outer join driver driver5_ on this_.driver_id=driver5_.driver_id left outer join place place6_ on this_.place_id=place6_.place_id left outer join recharge recharges9_ on this_.operation_id=recharges9_.operation_id left outer join record record8_ on this_.operation_id=record8_.operation_id left outer join sand_type sandtype7_ on this_.sand_type_id=sandtype7_.sand_type_id inner join vehicle vehicle2_ on this_.vehicle_id=vehicle2_.vehicle_id where 1=1 and 1=1 and (recharges9_.paid=false) and (this_.deleted=false and this_.visible=true) order by "+paraOrde+" ASC limit 1000"
            // Setter query
            JasperDesign jd = JRXmlLoader.load(uno)
            JRDesignQuery query = new JRDesignQuery()
            query.setText(stringQuery)
            jd.setQuery(query)
            //compiles jrxml
            JasperReport jr = JasperCompileManager.compileReport(jd)
            //fills compiled report with parameters and a connection
            JasperPrint print = JasperFillManager.fillReport(jr, reportParam, con)
            //JasperPrint print = JasperFillManager.fillReport(reportName +".jasper",reportParam)
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
            //}catch (ClassNotFoundException e){
            //    println("Driver de Postgresql no Cargado:"+e)
            //}catch (SQLException e){
            //    println("Error en la Consulta:"+e)
        } catch (Exception e) {
            render('something when wrong')
            throw new RuntimeException("No es posible generar el Reporte en el Formato solicitado.", e);
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            ex.printStackTrace();
        } finally {
            if(form_sali == 1) // Out Pdf
                render(file: pdfStream.toByteArray(), contentType: 'application/pdf')
            else if(form_sali == 4) // Out CSV{
            {
                response.setHeader("Content-disposition", "attachment; filename=listoper"+ "." + "csv")
                response.contentType = "application/CSV"
                response.characterEncoding = "UTF-8"
                response.outputStream << pdfStream.toByteArray()
            }
            else if(form_sali == 5) // Out XLS
            {
                response.setHeader("Content-disposition", "attachment; filename=listoper"+ "." + "xls")
                response.contentType = "application/vnd.ms-excel"
                response.characterEncoding = "UTF-8"
                response.outputStream << pdfStream.toByteArray()
            }
            else if(form_sali == 8) // Out DOCX
            {
                response.setHeader("Content-disposition", "attachment; filename=listoper.docx");
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                response.characterEncoding = "UTF-8"
                response.outputStream << pdfStream.toByteArray()
            }
            else if(form_sali == 2) // Out HTML
            {
                response.setHeader("Content-disposition", "attachment; filename=listoper.html");
                response.setContentType("text/html")
                response.characterEncoding = "UTF-8"
                response.outputStream << pdfStream.toByteArray()
            }
            else if(form_sali == 3) // Out Text
            {
                response.setHeader("Content-disposition", "attachment; filename=listoper.txt");
                response.setContentType("text/plain")
                response.characterEncoding = "ISO-8859-1"
                response.outputStream << pdfStream.toByteArray()
            }
            else if(form_sali == 9) // Out Ppt
            {
                response.setHeader("Content-disposition", "attachment; filename=listoper.pptx");
                response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation")
                response.characterEncoding = "UFT-8"
                response.outputStream << pdfStream.toByteArray()
            }
            else if(form_sali == 6) // Out Rtf
            {
                response.setHeader("Content-disposition", "attachment; filename=listoper.rtf");
                response.setContentType("text/rtf")
                response.characterEncoding = "UTF-8"
                response.outputStream << pdfStream.toByteArray()
            }
            else if(form_sali == 7) // Out Xml
            {
                response.setHeader("Content-disposition", "attachment; filename=listoper.xml");
                response.setContentType("text/xml")
                response.characterEncoding = "UTF-8"
                response.outputStream << pdfStream.toByteArray()
            }
        }
