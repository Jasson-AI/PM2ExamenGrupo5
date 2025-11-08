<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
file_put_contents("debug.txt", file_get_contents("php://input")); // Te crea un archivo en la carpeta con el JSON recibido


include_once 'conexion.php';
include_once 'Personas.php';

$db = new DataBase();
$instant = $db->getConnection();

$pinst = new Personas($instant);

$data = json_decode(file_get_contents("php://input"));


if(isset($data))
{

    $pinst->nombres = $data->nombres;
    $pinst->latitud = $data->latitud;
    $pinst->longitud = $data->longitud;
    $pinst->telefono = $data->telefono;
    $pinst->firma = $data->firma;

    if($pinst->createPerson())
    {
        http_response_code(200);
        echo json_encode( 
            array( "issuccess" => true,
            "message" => "Creado con exito"));
    }
    else
    {
        http_response_code(503); // Servicio no disponible
        echo json_encode( 
            array("issuccess" => false,
            "message" => "Error al crear"));
    }
}
else
{
    http_response_code(400);
    echo json_encode(array(
        "issuccess" => false,
        "message" => "Datos incompletos o inválidos"));

}



?>