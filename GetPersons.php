<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once 'conexion.php';
include_once 'Personas.php';

$db = new DataBase();
$instant = $db->getConnection();

$pinst = new Personas($instant);
$cmd = $pinst->GetListPersons();
$count = $cmd->rowCount();

if($count > 0)
{
    $personarray = array();

    while($row = $cmd->fetch(PDO::FETCH_ASSOC))
    {
        extract($row);
        $e = array(
            "id" => $id,
            "nombres" => $nombres,
            "telefono" => $telefono,
            "latitud" => $latitud,
            "longitud" => $longitud,
            "firma" => $firma
        );


        array_push($personarray, $e);
    }


    http_response_code(200);
    echo json_encode($personarray);
}
else
{
    http_response_code(404);
    echo json_encode( 
        array( "issuccess" => false,
               "message" => "No hay Datos")
    );
}

?>